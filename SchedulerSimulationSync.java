import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
// Import ReentrantLock for mutual exclusion
import java.util.concurrent.locks.ReentrantLock;
// Feature 3: Import Semaphore for blocking and signaling
import java.util.concurrent.Semaphore;

// ANSI Color Codes for enhanced terminal output (same as Assignment 1)
class Colors {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String WHITE = "\u001B[37m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
}

// ⚠️ DO NOT REMOVE OR MODIFY THIS CLASS ⚠️
// A race condition happens when a thread is preempted BETWEEN reading a shared value and writing it back.
// On a real machine this window is only a few nanoseconds wide, so the bug appears rarely and randomly
// (which is exactly what makes race conditions so dangerous!). To make the bug show up reliably in this
// assignment, the starter code calls RaceWindow.pause() in the unprotected code: it simulates the
// operating system preempting the thread at the worst possible moment.
class RaceWindow {
    public static void pause() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// ⚠️ SHARED STATISTICS: updated by ALL the process threads at the same time ⚠️
class SharedStats {
    static int totalProcesses = 0;
    static int totalCPUs = 0;
    static int contextSwitchCount = 0;
    static int completedProcessCount = 0;
    static long totalServiceTime = 0;
    static final java.util.concurrent.atomic.AtomicInteger busyWaitPolls = new java.util.concurrent.atomic.AtomicInteger(0);

    private static final ReentrantLock contextSwitchLock = new ReentrantLock();
    private static final ReentrantLock completedProcessLock = new ReentrantLock();
    private static final ReentrantLock serviceTimeLock = new ReentrantLock();

    public static void incrementContextSwitch() {
        contextSwitchLock.lock();
        try {
            int tmp = contextSwitchCount;
            RaceWindow.pause();
            contextSwitchCount = tmp + 1;
        } finally {
            contextSwitchLock.unlock();
        }
    }

    public static void incrementCompletedProcess() {
        completedProcessLock.lock();
        try {
            int tmp = completedProcessCount;
            RaceWindow.pause();
            completedProcessCount = tmp + 1;
        } finally {
            completedProcessLock.unlock();
        }
    }

    public static void addServiceTime(int ms) {
        serviceTimeLock.lock();
        try {
            long tmp = totalServiceTime;
            RaceWindow.pause();
            totalServiceTime = tmp + ms;
        } finally {
            serviceTimeLock.unlock();
        }
    }

    public static int getCompletedProcessCount() {
        completedProcessLock.lock();
        try {
            return completedProcessCount;
        } finally {
            completedProcessLock.unlock();
        }
    }

    public static int getContextSwitchCount() {
        contextSwitchLock.lock();
        try {
            return contextSwitchCount;
        } finally {
            contextSwitchLock.unlock();
        }
    }

    public static long getTotalServiceTime() {
        serviceTimeLock.lock();
        try {
            return totalServiceTime;
        } finally {
            serviceTimeLock.unlock();
        }
    }
}

// Ready Queue protected by ReentrantLock and Semaphore (Features 1 & 3)
class ReadyQueue {
    private final LinkedList<Thread> list = new LinkedList<>();
    private final Map<Thread, Process> map = new HashMap<>();
    private final ReentrantLock queueLock = new ReentrantLock();
    private final Semaphore availableProcesses = new Semaphore(0);

    public void add(Process process) {
        queueLock.lock();
        try {
            Thread thread = new Thread(process);
            list.add(thread);
            map.put(thread, process);
        } finally {
            queueLock.unlock();
        }

        availableProcesses.release();

        System.out.println(Colors.BLUE + "  ➕ " + Colors.BOLD + Colors.CYAN + process.getName() + Colors.RESET +
                Colors.BLUE + " (Priority: " + process.getPriority() + ") added to ready queue" + Colors.RESET +
                " │ Remaining: " + Colors.YELLOW + process.getRemainingTime() + "ms" + Colors.RESET +
                " of " + process.getBurstTime() + "ms");
    }

    public Thread poll() {
        queueLock.lock();
        try {
            if (list.isEmpty()) {
                return null;
            }
            Thread head = list.getFirst();
            RaceWindow.pause();
            list.removeFirst();
            return head;
        } finally {
            queueLock.unlock();
        }
    }

    public Process processOf(Thread thread) {
        queueLock.lock();
        try {
            return map.get(thread);
        } finally {
            queueLock.unlock();
        }
    }

    public String snapshot() {
        queueLock.lock();
        try {
            StringBuilder sb = new StringBuilder();
            for (Thread t : list) {
                if (sb.length() > 0) sb.append(" → ");
                sb.append(map.get(t).getName());
            }
            return sb.length() == 0 ? "empty" : sb.toString();
        } finally {
            queueLock.unlock();
        }
    }

    public void waitForProcess() throws InterruptedException {
        availableProcesses.acquire();
    }

    public void signalShutdown(int cpuCount) {
        availableProcesses.release(cpuCount);
    }
}

// Process representing an application task
class Process implements Runnable {
    private final String name;
    private final int burstTime;
    private final int priority;
    private int remainingTime;
    private int cpuId = -1;
    private boolean finished = false;

    public Process(String name, int burstTime, int priority) {
        this.name = name;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public String getName() { return name; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public int getRemainingTime() { return remainingTime; }
    public boolean isFinished() { return finished; }
    public void setCpuId(int cpuId) { this.cpuId = cpuId; }

    @Override
    public void run() {
        int quantum = 100;
        int timeSlice = Math.min(remainingTime, quantum);

        SharedStats.incrementContextSwitch();
        SharedStats.addServiceTime(timeSlice);

        try {
            Thread.sleep(timeSlice);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        remainingTime -= timeSlice;

        if (remainingTime <= 0) {
            finished = true;
            SharedStats.incrementCompletedProcess();
            System.out.println(Colors.GREEN + "  ✔ [CPU-" + cpuId + "] finished " + Colors.BOLD + Colors.BRIGHT_GREEN +
                    name + Colors.RESET + Colors.GREEN + " │ Completed: " + Colors.BRIGHT_WHITE +
                    SharedStats.getCompletedProcessCount() + "/" + SharedStats.totalProcesses + Colors.RESET);
        } else {
            System.out.println(Colors.YELLOW + "  ⏱ [CPU-" + cpuId + "] quantum expired for " + name +
                    " │ Remaining: " + remainingTime + "ms" + Colors.RESET);
        }
    }
}

// CPU Worker executing processes from the ready queue using Semaphore (Feature 3)
class CpuWorker extends Thread {
    private final int cpuId;
    private final ReadyQueue readyQueue;

    public CpuWorker(int cpuId, ReadyQueue readyQueue) {
        super("CPU-" + cpuId);
        this.cpuId = cpuId;
        this.readyQueue = readyQueue;
    }

    @Override
    public void run() {
        while (SharedStats.getCompletedProcessCount() < SharedStats.totalProcesses) {
            try {
                readyQueue.waitForProcess();
            } catch (InterruptedException e) {
                return;
            }

            Thread thread = readyQueue.poll();
            if (thread == null) {
                break;
            }

            Process process = readyQueue.processOf(thread);
            System.out.println(Colors.MAGENTA + "  ┌─ [CPU-" + cpuId + "] picks " + Colors.BRIGHT_CYAN +
                    process.getName() + Colors.MAGENTA + " │ Ready queue: [" + Colors.BRIGHT_WHITE +
                    readyQueue.snapshot() + Colors.MAGENTA + "]" + Colors.RESET);

            process.setCpuId(cpuId);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("CPU-" + cpuId + " interrupted.");
            }

            if (!process.isFinished()) {
                readyQueue.add(process);
            } else if (SharedStats.getCompletedProcessCount() >= SharedStats.totalProcesses) {
                readyQueue.signalShutdown(SharedStats.totalCPUs);
            }
        }
    }
}

// Main Simulation Runner
public class SchedulerSimulationSync {
    public static void main(String[] args) {
        System.out.println(Colors.BG_BLUE + Colors.BRIGHT_WHITE + Colors.BOLD +
                " === OS ASSIGNMENT 3: MULTI-CPU ROUND-ROBIN SCHEDULER (SYNCHRONIZED) === " + Colors.RESET);

        int cpuCount = 3;
        SharedStats.totalCPUs = cpuCount;

        ReadyQueue readyQueue = new ReadyQueue();

        List<Process> initialProcesses = new ArrayList<>();
        initialProcesses.add(new Process("Browser", 250, 2));
        initialProcesses.add(new Process("Compiler", 400, 1));
        initialProcesses.add(new Process("MediaPlayer", 150, 3));
        initialProcesses.add(new Process("TextEditor", 100, 4));
        initialProcesses.add(new Process("Database", 300, 1));

        SharedStats.totalProcesses = initialProcesses.size();

        System.out.println(Colors.BOLD + "\n[1] Initializing " + cpuCount + " CPU Workers..." + Colors.RESET);
        List<CpuWorker> cpus = new ArrayList<>();
        for (int i = 1; i <= cpuCount; i++) {
            CpuWorker cpu = new CpuWorker(i, readyQueue);
            cpus.add(cpu);
            cpu.start();
        }

        System.out.println(Colors.BOLD + "\n[2] Submitting processes to Ready Queue..." + Colors.RESET);
        for (Process p : initialProcesses) {
            readyQueue.add(p);
        }

        System.out.println(Colors.BOLD + "\n[3] Simulation running...\n" + Colors.RESET);

        for (CpuWorker cpu : cpus) {
            try {
                cpu.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(Colors.BG_GREEN + Colors.BRIGHT_WHITE + Colors.BOLD +
                " === SIMULATION COMPLETED SUCCESSFULLY == " + Colors.RESET);
        System.out.println("Total Processes Completed : " + SharedStats.getCompletedProcessCount());
        System.out.println("Total Context Switches    : " + SharedStats.getContextSwitchCount());
        System.out.println("Total Service Time (ms)   : " + SharedStats.getTotalServiceTime());
        System.out.println("Busy Wait Polls           : " + SharedStats.busyWaitPolls.get() + " (Must be 0)");
    }
}
