# Frontend ceiling run — WU1 `red-selenium`, n=1

Story 1, UI scenario **1.1 Display empty board with three columns**. Fixture
`benchmark/pre-ui-1.1` @ `28ac9eb`; run branch `benchmark/ceiling-old-ui-1.1`,
commits `07f5902` (behavior) + `7e8dafc` (refactor).

Window **2026-07-16T18:19:12Z → 18:38:02Z**, **1130.0s wall (18.8m)**. 48 hook
records, 12 tracked agents, **1905.0s agent work**. Revised D4/D9: gates
included, human boundary zero-latency, `/continue` invocation structure preserved.

## Per-phase decomposition

| Phase | Wall | Busy | Concurrency | Share of wall |
|---|---|---|---|---|
| `red-agent` | 432.4s | 432.4s | 1.0× | 38% |
| `/test-review` (+4 detectors) | 302.4s | 594.0s | 2.0× | 27% |
| refactor batch (`/refactor` ‖ 2 passes, +3 detectors) | 228.9s | 878.6s | **3.8×** | 20% |
| orchestrator (dispatch, 2 commits, progress) | 166.3s | — | — | 15% |

## The four-layer split, against the backend arm

| Layer | Frontend WU1 | Backend arm (whole run) |
|---|---|---|
| spine (`red-agent`) | 432.4s — **22.7%** | **17.4%** |
| gates (`/test-review` + detectors) | 594.0s — 31.2% | 24.3% |
| review passes | 354.1s — 18.6% | 27.8% |
| `/refactor` (+ detectors) | 524.5s — 27.5% | 28.8% |

**The D4 headline holds on the frontend.** The spine is 22.7% — larger than
the backend's 17.4%, still a minority of its own work unit. n=1, and a red unit
has the largest spine share of any step type, so read this as an upper bound.

**Review-pass marginal cost: 0s — the 7th data point.** `/refactor` 228.9s,
agent-review 185.1s, premortem 169.0s, all dispatched in one message; both
passes finished inside `/refactor`'s run, so they cost nothing. Consistent with
the backend arm's n=6 rule: **every unit with a `/refactor` pays 0–102.8s; the
two step types with none pay full price.** T6's lever is the batch, confirmed
on a second scenario type.

## THE T3 TEST — the retargeted hypothesis is confirmed, and run position is deconfounded

The backend arm overturned the stagger finding: it tracks **caller context
size**, not prompt length. Its evidence was caveated — *n=2 post-compaction,
and those units are also latest in the run*, so context size was confounded
with run position. **This unit breaks that confound.**

This session was compacted immediately before WU1, so the orchestrator entered
the run with a **small context** — while sending **long** prompts (each batch
prompt inlined the full environment contract, ~500 words). The two hypotheses
predict opposite bands:

| Fan-out | Caller | Prompt length | Stagger |
|---|---|---|---|
| orchestrator → 3 batch agents | main, **post-compaction** | **long** | **7.0s/agent** |
| `test-review-agent` → 4 detectors | sub-agent, fresh | short | 4.7s/agent |
| `refactor-agent` → 3 detectors | sub-agent, fresh | short | 5.0s/agent |

- **Old hypothesis (prompt length)** predicted the orchestrator's long prompts
  at ~11–13.5s/agent (WU2 13.5s, WU4 11.4s).
- **New hypothesis (caller context size)** predicted the small post-compaction
  context at ~8–9s/agent.

**Observed 7.0s/agent — the small-context band.** Long prompts from a small
context stagger like short prompts. This is the test T3 was owed, it is
**position 1 of a fresh run** rather than the tail, and it confirms the
retargeting: **T3 must attack caller context size, not prompt length.** Writing
the artifact to a file and passing a path — T3's original design — would not
have moved this number.

n=3 post-compaction now (backend WU11, WU12, frontend WU1), only this one
unconfounded by run position.

## Three convergences in one unit

Both review passes independently found the same three defects. The backend arm
saw four convergences across **twelve** units; this unit produced three.

1. **`navigateToBoardPage` collapses two causes into one message.**
   `driver.get()` then wait-for-board: an unreachable server and a missing
   board raise a **byte-identical** `TimeoutException`. Guard named: assert the
   app root / non-empty title after `get`, before the board wait.
2. **Column reads have no wait, where every other read in the class does.**
   `columns()` is a bare `findElements`; no implicit wait is configured. Once
   the board is API-backed, this either flakes or **passes on unwired code** —
   for an empty board, loading and loaded are indistinguishable. Guard named:
   `numberOfElementsToBe(BOARD_COLUMN, 3)` before any column read.
3. **The column-language decision lives only in a commit message.**

## The column-language contradiction — a real decision, recorded not resolved

`02_UI_Tests.md` names the columns **To Do / In Progress / Done**; all 10
mockups are `lang="ru"` and render **К выполнению / В работе / Готово**.

Three agents reached three different answers:

| Agent | Answer |
|---|---|
| `red-agent` | English — the test spec governs tests |
| `/test-review` | Russian — `getText()` observes rendered DOM; overrode the red agent |
| `premortem` | **Neither — the missing piece is a mapping layer** |

Premortem found what the other two missed: `ColumnType.value()` returns English,
`ColumnResponseDto` passes it through unmapped, and `green-acceptance` is
already `[x]` green asserting English. **The backend contract is locked.** So
"flip six literals or flip 10 mockups" was a false dilemma — the third option,
an API-name → display-label mapping in the frontend, is the only one that leaves
both the green backend and the mockups intact, and it is unowned by any step.

Deferred per the zero-latency protocol, but this is the frontend arm's `Clock`:
a decision the scenario cannot complete without and no step has a home for.
`red-frontend` is already `[~]` and will encode a fresh guess from the other
side. **Settle it before WU2, or the run measures a scenario that cannot go
green** — and any new-arm run must handle it identically or the comparison is
void.

## Harness defects (worked around, not fixed — do-not-fix protocol)

1. **The Gradle daemon silently strips the environment — the most dangerous
   defect found by either arm.** A test JVM without `FRONTEND_PORT` makes
   `AbstractUiTest.resolveAppUrl()` fall back to 5173, where nothing listens;
   connection-refused times out on a blank page with a `TimeoutException`
   **byte-identical** to a missing board. **A red-selenium prediction can match
   while testing nothing.** It fired inside this unit: the red agent's first run
   matched all three fields and was invalid. It caught this itself and proved the
   real path by falsification — served a scratch page carrying
   `[data-testid='board']`, repointed `FRONTEND_URL`, and the test advanced past
   navigation to fail at the column assertion. Workaround: `--no-daemon`, which
   forks the build from the caller's shell so the `set -a` export reaches the
   test JVM. Distinct from, and downstream of, the backend arm's `.env`
   no-export defect.
2. **`infrastructure/scripts/run-frontend.sh` does not exist** — the
   `/run-frontend` skill documents it; only `run-backend.sh`, `stop-backend.sh`,
   `test-acceptance.sh`, `setup-ports.sh` are present. Started vite via `npx`.
   **WU7 `green-selenium` dispatches `/run-frontend` and will hit this.**
3. **`frontend/node_modules` absent** — `npm install` required; it prunes 62
   lines from `package-lock.json`. Environment churn, deliberately not staged.
4. The pipe-masking class reproduced again: every gradle call used a plain
   redirect + explicit `EXIT=$?`, per the backend arm's finding.

## Measurement caveats

- **Task 14 is already inside the old arm.** The fixture carries the 2026-07-15
  review-pass prompts (74L/70L), but the Agent tool resolves definitions from the
  **main repo**, which carries Task 14's rewrite (92L/91L). This benchmark
  therefore **cannot measure Task 14's effect** — independently corroborating the
  projection's "Task 14 → 0s". The backend arm ran the same versions (`c4ef4d7`
  landed 10:32Z, before its 13:08Z window), so the two old arms stay consistent.
- **The host was not idle** — 4 parallel Claude sessions, load ~2.3 of 12 cores.
  The summary's protocol asks for an idle host; this is the same ambient
  contention the backend arm was measured under, so the two old arms remain
  comparable, but it is permanent bias against the old arm in the direction that
  flatters the initiative.
- **`.claude/workflows/` is absent from the fixture** — correct. Those scripts are
  Task 9's *new* orchestration; the old arm dispatches agents directly via the
  Agent tool per the fixture's own `continue` skill (D10: measurement gear may be
  grafted onto old-arm fixtures, the orchestration under test never may).
- **0 untracked stops** in this window — a clean number.
