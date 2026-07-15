# Task 9: Workflow-Based Deterministic Orchestration

Type: refactoring

Framework speed-up initiative **T2** — see
[framework-speedup/README.md](../../framework-speedup/README.md) (D1, D7) and
[checklist item C1](../../framework-speedup/checklist.md).

## Problem

`/continue` dispatch sequences are prose markdown tables that the model
re-interprets on every run. Parallelism written as "run X and Y in parallel" is
hoped-for, not guaranteed — the model may serialize, skip, or reorder. The
initiating concern (C1): "That is complex context engineering. If we could make
it somehow more deterministic that would be amazing."

Additionally, once sub-agents run concurrently, letting each one `git commit`
creates a commit race and destroys attribution.

## Solution

### Step 1: Workflow scripts for dispatch sequences

Convert the `/continue` work-unit dispatch sequences into Workflow tool scripts
(JS with `pipeline()`/`parallel()`), invoked by `/continue` instead of prose
fan-out instructions. Control flow (which agents, in what order, what runs
concurrently) becomes code, not interpretation. Steps that genuinely need model
judgment (e.g. adapters-discovery output) feed the script as `args`.

### Step 2: Single-committer discipline (D7)

Under Workflow orchestration, sub-agents never run `git commit`:

- The script is the single committer, landing commits **serially** at defined
  points — one commit per logical unit at today's granularity (red / green /
  refactor).
- Each agent returns the list of files it created/modified (structured output).
  The script stages by **pathspec** from that list; `git add -A` is forbidden
  while concurrent units are in flight.
- The script verifies claimed file lists against `git status` — a modified file
  claimed by no agent stops the run instead of being silently pooled.
- When two units write the same file, escalate that unit to worktree isolation
  and land its diff as its own commit.
- Semantic drift (rename vs freshly written test) is caught by the red
  prediction-mismatch rule.

### Step 3: Update the workflow rules

Reflect the new orchestration in `/continue` SKILL.md and
`workflow-detail.md`: dispatch tables point at scripts; commit discipline
section documents D7.

## Key Files

- `.claude/skills/continue/SKILL.md`
- `.claude/guidelines/workflow-detail.md`
- `.claude/workflows/` (new Workflow scripts)
- Agent definitions in `.claude/agents/` (structured file-list output)

## Dependencies

After Task 8 (baseline must be measured on the old orchestration first).
Enabler for Tasks 10, 11, 12. Coordinate stream-awareness with Task 16.
