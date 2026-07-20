# Frontend ceiling run — WU2–WU7, n=6 work units

Story 1, UI scenario **1.1 Display empty board with three columns**. Fixture
`benchmark/pre-ui-1.1` @ `28ac9eb`; run branch `benchmark/ceiling-old-ui-1.1`.
Continues `frontend-ui1.1-gated-red-selenium.md` (WU1).

Window **2026-07-16T18:45:47Z → 19:53:01Z**, **4034.5s wall (67.2m)**.
**40 agents dispatched, 40 matched a stop, 0 untracked.** **6745.0s agent work
(112.4m)**, mean concurrency **1.67×**. Revised D4/D9: gates included, human
boundary zero-latency, `/continue` invocation structure preserved.

**The arm stopped at 6 of 8 units.** WU7 `green-selenium` is BLOCKED and stays
`[~]`; WU8 `demo` never started. See "The blocker" below — it is the arm's
headline finding, not an incident.

Commits (all on the run branch): WU2 `16ad13a`+`93d95e7`, WU3 `bcff124`+`63aa295`,
WU4 `7ca9ca4`+`ff99ee9`, WU5 `bafe3eb` (no refactor commit), WU6 `4787e30` (no
refactor commit).

## THE HEADLINE — the four-layer split replicates across scenario types

| Layer | Frontend WU1 (n=1) | **Frontend WU2–7 (n=6)** | Backend arm (whole run) |
|---|---|---|---|
| spine (`red`/`green`) | 22.7% | **17.1%** (1151.3s) | **17.4%** |
| gates (`/test-review` + detectors, `/test-coverage`, `/design-review`) | 31.2% | 27.1% (1829.8s) | 24.3% |
| review passes | 18.6% | 29.4% (1980.8s) | 27.8% |
| `/refactor` (+ detectors) | 27.5% | 26.4% (1783.0s) | 28.8% |

**Spine 17.1% vs the backend's 17.4%.** Two different scenario types, two
different languages, two different test tiers — the same answer to within 0.3pp.
WU1's 22.7% was a single-red-unit artifact, exactly as that artifact caveated.
The work a human would call "the feature" is a **sixth** of what the run costs.

## THE T3 TEST — the strongest evidence yet, and it is intra-session

WU1 gave the retargeted hypothesis (caller **context size**, not prompt length)
its first unconfounded point at 7.0s/agent from a post-compaction context. This
window is the controlled follow-up: **same session, same orchestrator, same
prompt shapes, context growing monotonically**.

| Caller | Context | n | Mean stagger |
|---|---|---|---|
| **MAIN orchestrator** | post-compaction (WU1) | 3 | **7.0s** |
| **MAIN orchestrator** | grown, WU2–WU7 | 25 | **15.0s** |
| `test-review-agent` → detectors | fresh sub-agent | 3 | 9.7s |
| `refactor-agent` → detectors | fresh sub-agent | 3×4 | 6.3 – 10.0s |

The orchestrator's stagger **doubled (7.0 → 15.0s) within one session** as its
context grew, while sub-agent fan-outs launched from fresh contexts stayed flat
at 6–10s throughout. Prompt length did not change. Run position cannot explain a
sub-agent/orchestrator split measured over the same interval.

**T3 must attack caller context size.** Writing the artifact to a file and
passing a path — T3's original design — moves nothing.

## The review-pass marginal cost rule is WRONG, and this arm corrects it

The backend arm's n=6 rule was: *every unit with a `/refactor` pays 0–102.8s;
the two step types without one pay full price.* This arm falsifies the framing.

| Unit | `/refactor` | longest pass | **marginal** |
|---|---|---|---|
| WU2 | 253.7s | premortem 175.1s | **0s** |
| WU3 | 193.0s | premortem 228.4s | **+64s** |
| WU4 | 319.5s | premortem 261.2s | **0s** |
| WU5 | **94.0s (no changes)** | premortem 215.4s | **+151s** |
| WU6 | **91.9s (no changes)** | premortem 222.3s | **+165s** |

The predictor is not *"does the unit have a `/refactor` step"* — WU5 and WU6 both
had one. It is arithmetic:

> **marginal = max(0, longest_pass_end − refactor_end)**

The passes are free only when `/refactor` **outlasts** them. A `/refactor` that
finds nothing returns in ~92s and hides nothing, so the passes bill in full. The
backend's "0–102.8s" range was a sample where `/refactor` happened to be long.

**T6's lever is not "batch it" — batching is already done here.** The lever is
that the passes are ~200s of fixed cost the batch can only mask when there is
≥200s of refactor work to mask it behind. On a clean diff, nothing masks it.

## The blocker — WU7 `green-selenium` cannot run, and the rule predicted it

`green-selenium` stopped with **no verdict**: not green, not red. Nothing
started, nothing changed, `@Disabled` intact.

`infrastructure/.env` is **committed**, with `REPO_INDEX=2` — so `REPO_INDEX` is
per-**repo**, not per-**session**. Every worktree of this repo resolves to the
same 8082/5175. Port 8082 was held by the parallel ceiling-run session's backend,
and its board **is not empty**:

```
$ curl -s http://localhost:8082/api/v1/board
{"columns":[{"name":"To Do","tasks":[{"title":{"value":"Set up CI/CD"},...}]},...]}
```

`BoardPage` renders `[data-testid='board']` only after `fetchBoard()` resolves, so
running as configured would have asserted against **another session's data** and
reported badge `1` — a **false RED** indistinguishable from a real benchmark
failure. Proceeding required either killing a foreign process (forbidden) or
bending port discipline (a human's call). It stopped instead, which is correct.

**CLAUDE.md called this in advance.** The `.env`-resolution mechanism has not
landed and `unit-isolation-contract.md` does not exist in the fixture, so *"the
cap is zero — no service-touching unit may run in a worktree at all."* WU7 is a
service-touching unit in a worktree. This is not bad luck: two sessions on one
repo collide **by construction**, every time.

**Consequence for D4.** The old arm cannot measure `green-selenium` or `demo` in
a worktree. Either the ceiling run gives up worktree isolation for
service-touching units, or those units stay unmeasured in both arms — and if
they stay unmeasured, the arms remain comparable but the scenario's only
real-browser verification is outside the benchmark.

## Instrumentation defects found in the timing hooks themselves

Both are **measurement-integrity** bugs in Task 8's own gear, found while
reducing this window. Neither affects the numbers above (they are worked around),
but both silently corrupt the naive read:

1. **`dispatch` records carry an empty `agent_id`.** The id appears only on
   `return`. Joining `dispatch`→`subagent_stop` by `agent_id` therefore matches
   **1 of 40** agents and reports ~0s of work. The correct reduction is a
   three-way join: `dispatch`(`tool_use_id`) → `return`(`tool_use_id`→`agent_id`)
   → `subagent_stop`(`agent_id`).
2. **`return` fires at launch-ack, not completion** — 22ms after `dispatch` for a
   background agent. A `dispatch`→`return` duration measures the Agent tool
   acknowledging the launch, not the agent working. Real duration is
   `dispatch`→`subagent_stop`.

Together these mean any consumer computing agent time from `dispatch`/`return`
reads **~0.02s per agent** and a total near zero. Add to WU1's list; same class
as the harness defects below.

## The "silent success" defect class — now n=5 across the arm

Every one of these makes the harness **report success for work it never did**:

| # | Where | The lie |
|---|---|---|
| 1 | Gradle daemon strips env (WU1) | connection-refused ≡ missing board; a red prediction can match while testing nothing |
| 2 | `vite.config.ts` `process.env.BACKEND_PORT` never populated | silent `8080` fallback; real port 8082. Handler and client agree at the wrong value, so no test can see it |
| 3 | `@vitest/coverage-v8` absent | `vitest run --coverage` prints MISSING DEPENDENCY and **exits 0** — a non-run reads as a pass |
| 4 | timing hooks (above) | agent work computes to ~0s from a plausible-looking join |
| 5 | `REPO_INDEX` per-repo | a green/red verdict about a **neighbour's** code |

This is the frontend arm's most transferable finding. The defects are unrelated
in mechanism and identical in shape: **a default that is wrong, applied
silently.** Each was caught only because an agent went looking; none announced
itself.

## Other harness defects (worked around, not fixed — do-not-fix protocol)

- `infrastructure/scripts/run-frontend.sh` **does not exist** — `/run-frontend`,
  `/demo`, and `/test-acceptance` all route through it. Recorded in WU1;
  independently rediscovered by premortem in WU5. Would have hit WU7 and WU8.
- `run-backend.sh` and `test-acceptance.sh` both `export JAVA_HOME="/c/Program
  Files/OpenJDK/jdk-17.0.2"` — a **Windows path on a Linux host**.
- `test-acceptance.sh` exports `BACKEND_PORT`/`BACKEND_URL` but **not**
  `FRONTEND_PORT`/`FRONTEND_URL`; `AbstractUiTest.resolveAppUrl()` then defaults
  to 5173 — a neighbour's port.
- `vite.config.ts` sets no `server.port`/`strictPort` — vite silently
  auto-increments onto a free port when its default is taken.
- `.claude/templates/workflow/review-pass-contract.md` exists in **main** but not
  in the fixture. Agent definitions resolve from main and reference templates the
  older fixture lacks, so the review passes ran without their output contract.
  Same class as the Task 14 caveat.
- `/refactor`'s detector fan-out is **not deterministic**: it ran 3 detectors in
  WU2/WU3/WU4 and **none** in WU5/WU6, where the agent scanned the diff directly
  and flagged that no detector tables were passed to it.

## Measurement caveats

- **`/align-design` verify-only was SKIPPED in WU6.** `/refactor` produced zero
  changes, so the tree was byte-identical to the already-verified `4787e30`.
  Re-running would have cost ~400s for zero information. This **under-measures**
  the old arm — biased against the initiative, same direction as host contention.
- **The host was not idle** — a parallel ceiling-run session was active
  throughout (it is what blocked WU7). Same ambient contention as the backend
  arm, so the two old arms stay comparable.
- **Task 14 is already inside the old arm** (WU1's finding, unchanged): the
  fixture carries the 07-15 review-pass prompts but dispatch resolves main's
  post-Task-14 versions. The benchmark structurally cannot measure Task 14.
- **`.claude/workflows/` is absent from the fixture** — correct. Those are Task
  9's *new* orchestration; the old arm dispatches via the Agent tool per the
  fixture's own `continue` skill (D10).
