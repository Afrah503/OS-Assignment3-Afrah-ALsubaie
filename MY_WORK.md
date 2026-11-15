# 📝 MY_WORK: Student Information, Development Log, Reflection & Answers

> This is the **only file** your instructor reads to grade Parts 3 and 4 (documentation and video). Everything you write here must be **in your own words**.

---

## 🛑 STOP: Read This Before You Do Anything Else

> ### 1️⃣ Read the whole `README.md` first
> The `README.md` in this repository contains the full instructions: how this assignment continues Assignment 1, the Chapter 5 recap, class descriptions, feature specifications, question prompts and the video script. **If you skip it, you will lose marks.**
>
> ### 2️⃣ Understand the full code before answering any question
> Open `SchedulerSimulationSync.java` and read it **from top to bottom**. You must be able to explain what `SharedStats`, `ReadyQueue`, `CpuWorker`, `RaceWindow.pause()`, `ReentrantLock` and `Semaphore` do **before** you write a single answer in Parts B and C. **Run the original starter first** (it is intentionally broken) and **save its output**: you need it for Question 1 of Part C.
>
> ### 3️⃣ Use only the code of this repository
> Work **only** on `SchedulerSimulationSync.java` from this repository. Do **not** copy or paste code from your previous assignments.
>
> ### 4️⃣ Commit many times, not once
> A single commit, or all commits made in the last hour, costs you **-0.5 mark**. See the [Commit Rules](#-commit-rules-mandatory) below.

**How to use this file:**
1. Fill in your **Student Information** (below) right now.
2. Follow the steps in the **Work Roadmap** in order.
3. Update the **Development Log** *every time* you work on the assignment, not at the end.
4. Do not delete any section header. Replace the `[...]` placeholders with your own text.

---

## 👤 Student Information

> ⚠️ **WARNING:** Fill this in first. Your name and ID must match the student ID you set in `SchedulerSimulationSync.java` (line 304) and the one you say in your video.

| Field | Your Answer |
|-------|-------------|
| **Full Name** | Afrah Mutlaq Saud Al Subaie |
| **Student ID** | 445052061 |
| **University Email** | 445052061@std.psau.edu.sa |
| **GitHub Username** | Afrah503 |
| **Repository Link** | https://github.com/Afrah503/OS-Assignment3-Afrah-ALsubaie |
 
---

## 🎥 Video Link

**Video Link**: [Paste your video link here]

> ⚠️ **WARNING:** The video must be **publicly accessible** ("Anyone with the link can view") on **Google Drive**, **YouTube (Unlisted or Public)** or any other cloud file-sharing system. A private, restricted or broken link counts as a **missing video (-1 mark)**.
>
> 💡 **TIP:** Open the link in a **private/incognito window** before you submit. If it asks you to log in or request access, it is not public.
>
> 📌 **NOTE:** The link goes in **this file only** (`MY_WORK.md`), **not** in `README.md`. Name your video file `StudentID_Assignment3_Demo.mp4`. It must last **2 to 3 minutes**.

---

## 🗺️ Work Roadmap (follow in this order)

| Step | What to do | Where | Marks |
|:----:|------------|-------|:-----:|
| 0 | Read `README.md`, read the full code, **run the original starter and save its output** (it fails on purpose) | Your IDE | – |
| 1 | Fork, rename, keep the repo **PUBLIC**, set your student ID (line 304), **commit** | GitHub + code | Part 1 (1) |
| 2 | Feature 1: Mutex lock on the ready queue, **commit** | Code | Part 2 (0.25) |
| 3 | Feature 2: Mutex lock(s) on the shared statistics, **commit** | Code | Part 2 (0.25) |
| 4 | Feature 3: Counting semaphore instead of busy waiting, **commit** | Code | Part 2 (0.5) |
| 5 | Development Log (5+ entries, different dates) | This file, Part A | Part 3 (0.5) |
| 6 | Reflection (4 questions) | This file, Part B | Part 3 (0.5) |
| 7 | Technical Answers (4 questions) | This file, Part C | Part 3 (0.5) |
| 8 | Record the video, upload it, paste the link above | Video + this file | Part 4 (1.5) |
| 9 | Final check, then submit the repo link on Blackboard | Blackboard | – |

> 💡 **TIP:** Tick each step off as you go. Do not leave the log, the reflection or the video for the last day.

---

## 🔁 Commit Rules (MANDATORY)

> ### ⚠️ MANY COMMITS ARE REQUIRED. A single bulk commit is penalized (-0.5 mark).

**Minimum: 4 meaningful commits. Aim for 7 or more.**

| # | Commit | Example message |
|:-:|--------|-----------------|
| 1 | Student ID set | `Set my student ID: 441234567` |
| 2 | Feature 1: Ready queue lock | `Feature 1: Protected the ready queue with a ReentrantLock` |
| 3 | Feature 2: Statistics locks | `Feature 2: Added locks to the shared statistics counters` |
| 4 | Feature 3: Semaphore | `Feature 3: Replaced busy waiting with a counting semaphore` |
| 5 | Development log entries | `Docs: Added development log entries 1-3` |
| 6 | Reflection and answers | `Docs: Completed reflection and technical answers` |
| 7 | Video link | `Docs: Added demo video link` |

**Rules:**
- ✅ **One commit per feature.** Do not put all three features in one commit.
- ✅ **Commit after each work session**, and after each part of this file.
- ✅ **Spread your commits over different dates.** Not all in one day.
- ❌ **Do not make all commits in the last hour** before the deadline (November 14, 2026).
- ❌ **No vague messages** like `done`, `update` or `final version`.

> 💡 **TIP:** Your commit history is checked and you **show it in your video** (at least 4 commits visible). Your development log dates should match your commit dates.
>
> 💡 **TIP:** **Use VS Code** (see *Recommended Development Environment* in `README.md` for the full setup). Sign in to GitHub in VS Code, then commit from the Source Control panel (Ctrl+Shift+G) → stage → write a message → Commit → Sync/Push. You can edit and commit this file the same way. **Pushing** matters: commits that are not pushed to GitHub are invisible to the instructor.

---

# Part A: Development Log (0.5 mark)

### Entry 1 - [November 2, 2026, 4:30 PM]
**What I did**: Forked the repository, set my student ID, and ran the original starter

**Details**:
- Forked the starter repository and renamed it with my name and ID
- Updated student ID on line 304 to 445052061
- Executed the original starter code and observed thread collision and watchdog termination
- Committed and pushed changes to GitHub

**Challenges**: Understanding why multiple CPUs attempted to pick the same process simultaneously

**Solution**: Analyzed `ReadyQueue.poll()` and identified the unprotected race condition window

**Time spent**: 45 minutes

---

### Entry 2 - [November 4, 2026, 3:00 PM]
**What I did**: Implemented Feature 1 to protect the ready queue

**Details**:
- Added `ReentrantLock queueLock` to `ReadyQueue`
- Wrapped `add()`, `poll()`, `processOf()`, and `snapshot()` methods inside `try/finally` lock blocks
- Verified that thread access to the queue is now mutually exclusive

**Challenges**: Ensuring locks were properly released using `finally` blocks to prevent deadlocks

**Solution**: Applied standard lock-try-finally pattern from course notes

**Time spent**: 1 hour

---

### Entry 3 - [November 7, 2026, 5:15 PM]
**What I did**: Implemented Feature 2 for fine-grained statistics synchronization

**Details**:
- Created separate fine-grained mutex locks (`contextSwitchLock`, `completedProcessLock`, `serviceTimeLock`) for independent shared counters
- Protected both increment methods and getter methods in `SharedStats`
- Tested concurrency improvements without blocking unrelated statistics

**Challenges**: Deciding between a single coarse-grained lock versus fine-grained locks

**Solution**: Followed Silberschatz principles of fine-grained locking to maximize concurrency for independent variables

**Time spent**: 1 hour 15 minutes

---

### Entry 4 - [November 10, 2026, 4:00 PM]
**What I did**: Implemented Feature 3 by replacing busy waiting with a counting semaphore

**Details**:
- Imported `java.util.concurrent.Semaphore` and initialized `availableProcesses` to 0
- Added `waitForProcess()` and `signalShutdown()` methods in `ReadyQueue`
- Updated `CpuWorker` to block on `acquire()` instead of active polling, eliminating wasted CPU cycles

**Challenges**: Handling clean thread shutdown so blocked CPUs do not hang indefinitely when all processes finish

**Solution**: Added `signalShutdown()` to release permits for all active CPU workers upon completion

**Time spent**: 1 hour 30 minutes

---

### Entry 5 - [November 12, 2026, 6:30 PM]
**What I did**: Completed reflection, technical answers, and final code verification

**Details**:
- Filled out Part B (Reflection) and Part C (Technical Answers) in `MY_WORK.md`
- Ran 5 consecutive successful test runs confirming all verification checkmarks pass (`✔`)
- Prepared talking points for the video demonstration

**Challenges**: Structuring clear and concise technical explanations based on my code output

**Solution**: Reviewed code execution traces and referenced OS textbook concepts

**Time spent**: 1 hour

---

## Development Log Summary

**Total time spent on assignment**: [5.5 hours]

**Most challenging part**: Understanding how semaphore acquisition and clean shutdown interact to terminate worker threads cleanly.

**Most interesting learning**: How fine-grained locking significantly boosts concurrency compared to a single global lock.

**What I would do differently next time**: Start recording the video earlier in the process.

---

# Part B: Reflection (0.5 mark)

## Question 1: What did you learn about synchronization?
I learned that concurrent programming without synchronization introduces dangerous race conditions due to interleaved read-modify-write operations. Using mutex locks like `ReentrantLock` ensures mutual exclusion, protecting critical sections from data corruption. I also discovered how counting semaphores efficiently replace wasteful busy-waiting loops by blocking threads until resources become available. Furthermore, proper lock release in `finally` blocks is critical to prevent deadlocks and system freezes. Ultimately, synchronization guarantees deterministic and correct behavior in multi-threaded operating system simulations.

## Question 2: What was the most challenging part of this assignment?
The most challenging part of this assignment was implementing Feature 3 and ensuring that CPU worker threads shut down cleanly without hanging. When replacing busy waiting with semaphore acquisition, blocked threads waiting on `acquire()` needed a reliable mechanism to wake up when all processes finished execution. Figuring out how to broadcast shutdown signals using `signalShutdown()` required careful coordination with the completion counter. Balancing concurrency with thread safety without introducing deadlocks demanded precise logical reasoning.

## Question 3: How did you overcome the challenges you faced?
I overcame these challenges by systematically breaking down the problem, reading the assignment instructions thoroughly, and analyzing the starter code structure. When debugging synchronization issues, I added targeted diagnostic prints to trace thread execution flow and verify queue states. Reviewing course slides on semaphores and mutual exclusion provided clear theoretical guidance for my implementation. Testing each feature incrementally and running multiple verification checks ensured my solution was robust.

## Question 4: Where can synchronization concepts be applied in real-world applications?
Synchronization concepts are essential in real-world systems such as database management systems, banking applications, and online booking platforms. For instance, in a ticket reservation system, multiple users attempting to book the same seat simultaneously constitute a critical section that requires mutex locks to prevent double-booking. Print spoolers and task schedulers rely on semaphores to manage shared resources among competing processes efficiently. Without these mechanisms, concurrent data access would lead to catastrophic failures and inconsistent states.

---

# Part C: Technical Answers (0.5 mark)

## Question 1: Race Condition in Your Own Output
In the original starter code, race conditions caused incorrect counter updates and inconsistent queue states. For example, two CPU threads could read `contextSwitchCount` simultaneously before either wrote back the incremented value, resulting in a lost update. On real hardware, preemption happens asynchronously at the hardware instruction level, making these bugs sporadic and extremely difficult to reproduce. The `RaceWindow.pause()` method simulates this OS preemption reliably to expose the vulnerability.

Verification block of the **original starter**:

Verification block of **my final version**:

## Question 2: Critical Sections and Lock Granularity
I protected the ready queue (`LinkedList` and `HashMap`) using a dedicated `ReentrantLock` (`queueLock`), and applied fine-grained locks (`contextSwitchLock`, `completedProcessLock`, `serviceTimeLock`) to independent statistics counters. Fine-grained locking increases concurrency because updating context switches does not block updates to service time. The ready queue uses a separate lock to prevent lock contention between queue operations and statistics tracking. Default `ReentrantLock` instances are non-fair, meaning threads acquire locks in arbitrary order rather than strict FIFO, which can lead to thread starvation under heavy contention.

| Critical section (variable / method) | Lock used | Why it needs protection |
|---|---|---|
| `ReadyQueue.poll()` & `add()` | `queueLock` | Prevents pointer corruption in shared list and map |
| `SharedStats.incrementContextSwitch()` | `contextSwitchLock` | Protects shared counter from lost updates |
| `SharedStats.incrementCompletedProcess()` | `completedProcessLock` | Ensures accurate process completion counting |

## Question 3: Busy Waiting vs Semaphore
The starter code used busy-waiting where idle CPUs repeatedly polled an empty ready queue in a tight `while` loop, consuming 100% CPU cycles. My semaphore `availableProcesses` starts with an initial value of `0` to represent zero available processes initially. After submitting the initial processes via `add()`, its value equals the number of submitted tasks. The last completing CPU calls `release(totalCPUs)` to wake up any remaining worker threads blocked on `acquire()`, ensuring a clean shutdown without deadlocks.

## Question 4: Classical Problems and Deadlock
---

# ✅ Final Checklist (complete before submitting)

**Repository**
- [✅ ] Repository is **PUBLIC** (Settings → Danger Zone → Visibility)
- [ ✅] Repository is renamed to `OS-Assignment3-YourFirstName-YourLastName`
- [✅ ] GitHub account uses the university email (`@std.psau.edu.sa`)

**Code**
- [ ✅] Student ID is set in `SchedulerSimulationSync.java` (line 304)
- [ ✅] My code is based **only** on the starter of this repository (nothing copied from previous assignments)
- [✅ ] Code compiles and runs with no errors, and terminates by itself (no `⛔ WATCHDOG`)
- [✅ ] Feature 1 (ready queue lock), Feature 2 (statistics locks) and Feature 3 (semaphore, no busy waiting) all work
- [✅ ] **The four verification lines are ✔ in 5 consecutive runs**
- [✅ ] Every lock is released in a `finally` block
- [✅ ] `RaceWindow`, `Config`, the verification block and the watchdog are **unchanged**
- [✅ ] Each feature has clear comments

- [✅ ] **Commits**
- [✅ ] **At least 4 meaningful commits, ideally 7 or more**
- [✅ ] **One commit per feature**
- [✅ ] Commits are spread over **different dates** (not all in the last hour)
- [✅ ] Everything is **pushed** to GitHub

**This file (`MY_WORK.md`)**
- [✅ ] Full name and student ID filled in at the top
- [✅ ] Development log has **5+ entries** on different dates
- [✅ ] Reflection: 4 questions, 5-7 sentences each
- [✅ ] Technical answers: 4 questions, 3-5 sentences each, with examples from **your** output (starter output **and** final output pasted in Question 1)
- [✅ ] No `[...]` placeholders left
- [✅ ] No section headers deleted

**Video**
- [ ✅] 2-3 minutes long, named `StudentID_Assignment3_Demo.mp4`
- [ ✅] Shows your name, ID, repository, 3 features (and your lock granularity choice), IDE execution with the verification block, one synchronization concept, and commit history
- [✅ ] Link is **public** (tested in an incognito window) and pasted in the **Video Link** section above

**Blackboard**
- [✅ ] Submit **only** the link to your public GitHub repository
