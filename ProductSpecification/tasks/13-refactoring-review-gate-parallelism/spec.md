# Task 13: Review-Pass + Gate-Agent Parallelism

Type: refactoring

Framework speed-up initiative **T6** — see
[framework-speedup/README.md](../../framework-speedup/README.md) and
[checklist items 1.2b, 1.2c, 1.3, 1.4, 1.7, 1.8](../../framework-speedup/checklist.md).

## Problem

The review agents added at the end of every `/continue` step cost several
minutes each — the single most-felt slowdown. Several parallelism claims are
designed-in but unverified:

- Do refactor detection clusters (M/D/T) and test-review clusters (A/P/Se/S)
  actually fan out, or does the model serialize them? (1.4, 1.7)
- Do the pre-commit review passes (agent-review + premortem) really run in
  parallel with the `/refactor` batch as SKILL.md says? (1.8)
- Which quality gates can overlap each other and the review passes? (1.2b, 1.3)

Ceiling note: a gate cannot precede the agent whose output it inspects — the
parallelizable part is **detection**, not application.

## Solution

### Step 1: Confirm with Task 8 data

Read the per-step time-budget table; identify which review passes and gates
actually run serially and what the serial tail costs.

### Step 2: Enforce fan-out in the Workflow scripts

Where measurement shows serialization, encode the parallelism in the Task 9
scripts instead of prose: detection clusters via `parallel()`, review passes
concurrent with the step's quality gates.

### Step 3: Start review passes earlier

Feed review passes a diff snapshot so they start before the behavior commit
lands, instead of waiting for it.

### Step 4: Speed up the agents themselves

Per-agent cost reductions guided by the data: trim loaded context (only the
relevant layer/cluster files), lower model/effort tier for mechanical
detection clusters, keep high tier only where judgment quality matters.

Also feeds the open gate-pruning concern: the same data says which gates don't
earn their cost — pruning is a separate decision with numbers, not part of
this task.

## Key Files

- `.claude/skills/continue/SKILL.md` (Pre-Commit Review Passes)
- `.claude/agents/refactor-*.md`, `.claude/agents/test-review-*.md`
- `.claude/agents/agent-review-agent.md`, `.claude/agents/premortem-agent.md`
- `.claude/workflows/` (scripts from Task 9)

## Dependencies

After Task 8 (data) and Task 9 (scripts to encode the fan-out in).
