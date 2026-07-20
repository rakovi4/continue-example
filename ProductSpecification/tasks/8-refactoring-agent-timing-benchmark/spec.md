# Task 8: Agent Timing Instrumentation + Ceiling Benchmark

Type: refactoring

Framework speed-up initiative **T1** — see
[framework-speedup/README.md](../../framework-speedup/README.md) (D4, D8) and
[checklist items 1.2a, C2, C3](../../framework-speedup/checklist.md).

## Problem

Every impact figure in the speed-up initiative is a hypothesis. Review agents
are observed to add "several minutes" per `/continue` step, but nothing is
measured. Two gaps:

1. **No objective timing data.** There is no record of how long each agent and
   subagent run takes per work-unit step. Any estimate of "what to parallelize
   first" is vibes. Timings must be **hook-emitted by the harness** (stamped at
   agent dispatch and return), NEVER agent-self-reported — the footprint must be
   immune to hallucination.
2. **No benchmark fixture to reset to.** The ideal benchmark (D4) needs a fixed
   starting commit, but implementation history was squashed into the initial
   template commit — there is no historical commit representing "just before
   scenario 3.1".

## Solution

### Step 1: Hook-emitted agent timings

Add PreToolUse/PostToolUse hooks on the Agent tool in `.claude/settings.json`
that append one JSONL record per event (timestamp, event kind, agent type,
description/label) to a per-session timing log. No prompt or agent behavior
changes — pure harness-side instrumentation.

### Step 2: Time-budget aggregation

A script that folds the timing log into a per-step time-budget table: for each
work-unit step and agent type, wall-clock duration, degree of actual overlap,
and the serial tail. This table is the required output format (checklist C2) —
raw logs alone don't answer "what does each step cost".

### Step 3: Synthetic benchmark fixtures (D8)

One-time, no-review replay of story 1 scenarios 1.1→2.3 starting from the
template commit; commit the result as branch `benchmark/pre-3.1`. Continue the
replay through the rest of story 1 backend to produce `benchmark/pre-ui-1.1`
(frontend fixture). Fairness requires only that old and new runs start from the
**identical commit**, not a historical one. The replay also shakes out the
benchmark harness before any timed run.

### Step 4: Ceiling baseline runs (D4, revised per D9)

Red→green-acceptance runs with instrumentation on, current ("old")
implementation:
- Backend: story 1 scenario **3.1 Create task with title only** from
  `benchmark/pre-3.1` (richest path: coverage red + mid-scenario refactor +
  both adapter pairs — exercises exactly what Tasks 10 and 12 change).
- Frontend: story 1 first UI scenario from `benchmark/pre-ui-1.1`.

**Gates are included.** `/test-review`, `/refactor`, and both pre-commit review
passes run exactly as they do in normal work. They are the largest thing the
restructure moves; a run that skips them measures only the agent spine and
scores the winning idea at zero (D9).

**Human time is excluded; human round-trips are not.** The run fires
`/continue` with zero latency at each boundary where a human would be asked to
look. That boundary count is a structural property of the sequence under test:

| Arm | `/continue` invocations | Boundary |
|---|---|---|
| Old | 11 | one per work unit — every `[x]` in scenario 3.1's progress.md |
| New | 3 | one per stage — design approved / usecase reviewed / final result |

Each invocation costs real agent time (context reload, `progress.md` re-read,
skill preamble, ADR re-read) even with an instant human, and steps inside one
invocation can be parallelized while steps across two cannot. Excluding human
latency keeps the number measurable; preserving the invocation count keeps the
comparison honest.

Record the baseline time-budget table in the framework-speedup README, with a
per-arm invocation count beside the wall-clock. The "new implementation" runs
repeat on the same fixtures after Tasks 9–12 land.

### Known fixture limitation — the replayed 3.1 is not the historical 3.1

The synthetic fixture (D8) reproduces the *scenario*, not the *session that
implemented it*. Measured divergence, recorded here so the baseline is read
with it in view:

- **Coverage gaps: 0 replayed vs 2 in history.** Story 1's history shows two
  usecase-layer coverage gaps, both resolved as `[S]` skipped greens ("code
  already handles null, test passes in RED"). The replay produces none, so the
  baseline under-measures exactly the step Task 12 changes.
- **Steps: 11 replayed vs 14 in history.** History's 3.1 contains a second
  `design` + ADR (`board-aggregate-mutation-decision.md`) and a
  `refactor-usecase` Board-aggregate pivot that the replay never reproduced.

The benchmark's 3.1 is therefore a **cleaner, shorter scenario than the one
that happened** — 51m30s is optimistic as "what a backend scenario costs".
Old-vs-new stays fair under D8 (both arms start from the identical commit), so
this does not invalidate the comparison; it bounds what the absolute number
may be quoted as. Closing the gap means seeding the fixture so coverage fires —
tracked as its own decision, not silently absorbed into the baseline.

## Key Files

- `.claude/settings.json` (hook registration)
- `.claude/hooks/` (new timing hook script)
- `ProductSpecification/framework-speedup/README.md` (baseline table lands here)
- Branches `benchmark/pre-3.1`, `benchmark/pre-ui-1.1` (created by Step 3)
