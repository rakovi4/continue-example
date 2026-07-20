# red-usecase (coverage) 3.1, gated — WU5 measurement (n=1)

Fifth work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commit
`8cc20bf`. Window **2026-07-16T15:45:43Z → 16:00:40Z = 897s (14m57s)**. `/refactor`
returned NO ACTION, so the unit produced **one** commit.

This is the first of the two steps WU4's coverage gate inserted: a test pinning that a
board without a TO_DO column throws `ColumnNotFoundException`.

| Artifact | What |
|---|---|
| `backend-3.1-gated-red-usecase-coverage-wu5-timings.jsonl` | hook records, WU5 window (44) |
| `backend-3.1-gated-red-usecase-coverage-wu5-progress.txt` | per-milestone stamped progress log |

**Untracked stops: 2** in this window — busy totals under-count by that much.

## Where the 897s went

| Phase | Wall | Share |
|---|---|---|
| **red-agent** | 211.1s | 24% |
| Gap: red return → gate dispatch | 22s | 2% |
| **`/test-review`** (detector fan-out + 3 serial fixes + verify) | 360.9s | 40% |
| Behavior commit | 29s | 3% |
| Gap: commit → batch dispatch | 19s | 2% |
| **Refactor batch** — refactor 254.8s ‖ agent-review 163.1s ‖ premortem 179.5s | 254.8s | 28% |

## The gate is again the most expensive phase — and by the widest margin yet

`/test-review` (360.9s) is **1.7× red-agent** (211.1s). The trend across the run's red
units: WU3 1.4×, WU8 1.3×, WU10 1.7×, WU5 1.7×. The gate has never been cheaper than the
agent it audits, in any red unit, at any layer.

## The passes cost zero marginal seconds — the first such unit

The batch ran **961.3s of agent work in 254.8s wall (3.77×)**, the highest overlap ratio
of the run. `refactor-agent` returned **last** (16:00:40) — after premortem (15:59:50) and
agent-review (15:59:22) — so the passes cost a **marginal 0s**. `serial_tail=0s` in the
report.

This is the cleanest confirmation of WU3/WU4's rule: the passes' marginal cost is whatever
premortem overruns `/refactor` by, and here it overran by nothing. Note the ratio is
highest on a NO-ACTION `/refactor` — the detectors still fanned out and still cost their
full 363.9s of work; NO ACTION describes the *verdict*, not the effort.

## Two rulings worth preserving

**The gate corrected the red-agent's cited precedent.** red-agent justified calling
`setBoard` from a test body by pointing at `initFakes`. `/test-review` ruled the precedent
inapt — `initFakes` is `@BeforeEach` wiring that no test body calls, so it licenses *where
the call may live*, not *what a test body may call*. The ruling then stood on rule-strength
alone rather than on the bad precedent.

**A placement collision was ruled ACCEPT.** Cluster P flagged
`givenBoardWithoutToDoColumn` sitting on `ApplicationTest`; the gate accepted red-agent's
trade-off and left `ApplicationTest` unmodified. Three fixes were applied across two files
(`isInstanceOf` → `isExactlyInstanceOf` + `as()` descriptor; a Statements rename; the call
site). `isNotInstanceOf(ValidationException)` was **kept** as an ADR tripwire with a
rationale comment.

## A misleading short-circuit in the gate's own tooling

`/test-review` initially reported "none" from a `&&` chain that short-circuited. The real
cause: JUnit's XML `name` attribute holds the **DisplayName**, not the method name, so the
grep could never match. Confirmed by reading the XML directly. Adjacent to the run's other
tooling defects — the tooling that audits the tests is not itself audited.

## Verdicts (both non-gating; commit `8cc20bf` stands)

- **agent-review: CONCERNS.** Statements API misuse surface around
  `createTaskWithValidTitle` — the rename made the method's name promise a valid-title
  creation while its only caller uses it to drive an error path.
- **premortem: CONCERNS.** The S3 read-after-write orphan again — **three-for-three** at
  this point in the run.

## Carry-over

- **Nothing was fixed.** Measurement run; the passes are non-gating.
- Tests-only commit: no production file touched, so the unbootable-context finding from
  WU3 neither improved nor worsened here.
- No backend was started — WU5 is **not** contaminated by the Task 18 Step 0 breakage.
- The per-milestone stamp addendum held with no misses.
