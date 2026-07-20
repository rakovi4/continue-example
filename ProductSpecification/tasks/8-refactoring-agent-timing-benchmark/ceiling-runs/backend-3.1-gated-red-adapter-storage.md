# red-adapter storage 3.1, gated — WU8 measurement (n=1)

Eighth work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commits
`7207669` (behavior) + `91bfeb2` (refactor). Window **2026-07-16T16:02:51Z → 16:22:36Z =
1185s (19m45s)**. The most expensive unit of the run after WU11's compaction-inflated
window.

First of the four steps WU7's `adapters-discovery` inserted.

| Artifact | What |
|---|---|
| `backend-3.1-gated-red-adapter-storage-wu8-timings.jsonl` | hook records, WU8 window (48) |
| `backend-3.1-gated-red-adapter-storage-wu8-progress.txt` | per-milestone stamped progress log |

**Untracked stops: 0.** 12 agents, 12 starts, 12 stops — the `progress-hooks.md` known
limitation does not bite this window.

## Where the 1185s went

| Phase | Wall | Share |
|---|---|---|
| **red-agent** | 289.0s | 24% |
| Gap: red return → gate dispatch | 22s | 2% |
| **`/test-review`** (5 detectors incl. selenium; 0 findings; 2 verification runs) | 383.5s | 32% |
| Behavior commit | 20s | 2% |
| Gap: commit → batch dispatch | 17s | 1% |
| **Refactor batch** — refactor 328.0s ‖ agent-review 207.2s ‖ premortem 402.5s | 430.8s | 36% |
| Tail (refactor commit) | 23s | 2% |

## The finding: two fresh contexts convergently falsified the commit's central claim

The commit's whole point was a write-here-read-there storage test. Both passes
**independently** established that **the test never reads the database**:

`@DataJpaTest` is meta-annotated `@Transactional`. `findAll()` auto-flushes the INSERT, but
Hibernate then resolves each row's `EntityKey` against the still-open persistence context
and returns the **already-managed instance**, discarding the hydrated state. Every asserted
field originates from the object the test wrote. The test would pass against a `save()`
that dropped every column on the floor.

**Premortem went one step further and falsified `/test-review`'s own disclosed fix.** The
gate had noted the test's second-precision `Instant` and proposed sub-second precision as
the fix. Premortem showed that cannot work: the persistence-context cache hides truncation
regardless of precision, so the proposed fix addresses a symptom the mechanism never
produces.

`coverage-agent` then confirmed it **first-hand at WU9**, making this the **third
independent instance of the executed-vs-pinned pattern** in the run — code that reports
100% coverage precisely because the test executes it without asserting on it.

This is the run's strongest evidence for the review passes' value. `/test-review` ran five
detectors, returned **0 findings**, and ruled the harness deviation ACCEPTED. The gate was
looking at assertion strictness and placement — real checks, all passed. The defect was
one layer beneath: the assertions were strict, well-placed, and **structurally incapable
of failing**. Two fresh contexts, differently prompted, both found it. Neither gate could
have.

## Batching: premortem overran `/refactor` by 103s

The batch ran **1298.6s of agent work in 430.8s wall (3.01×)**. `refactor-agent` returned
at 16:20:30; premortem at 16:22:13. The passes' marginal cost is therefore **102.8s** —
the largest of any unit that *had* a `/refactor` to hide behind, and still 3× cheaper than
the 338.8s/314.2s that WU2 and WU12 paid with nothing to overlap.

Premortem (402.5s) was the run's single longest review-pass agent.

## A near-miss refactor that would have been an active bug

`/refactor` considered hoisting the duplicated `getBoard()` call into one shared local.
**The duplication is load-bearing.** `H2BoardStorage` rebuilds the board from `findAll()`
on every call, and `Column.append` mutates in place — so a single shared local would let
the test pass **even if `save` did nothing at all**. The detector correctly declined.

This is the second time in the run that an obvious-looking cleanup would have destroyed a
test's ability to fail. Both times the mechanism was the same: state that looks shared is
actually rebuilt per call.

## Verdicts (both non-gating; commits stand)

- **agent-review: CONCERNS** — the persistence-context finding above.
- **premortem: CONCERNS** — the same mechanism, reached independently, plus the
  falsification of the gate's proposed fix.

## Carry-over

- **Nothing was fixed.** Measurement run; the passes are non-gating. A real run would owe
  a `flush()`/`clear()` or a native re-read before trusting any storage assertion.
- `StorageAccessTestConfiguration.java` was **created in this unit and deleted in WU9's
  refactor** — the harness it introduced did not survive one work unit.
- No backend was started (`@DataJpaTest` runs an embedded context) — WU8 is **not**
  contaminated by the Task 18 Step 0 breakage.
- The per-milestone stamp addendum held with no misses.
