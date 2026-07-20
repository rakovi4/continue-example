# red-usecase 3.1, gated — WU3 measurement (n=1)

Third work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commit
`dec1457`. Window **2026-07-16T14:55:28Z → 15:12:24Z = 1016s (16m56s)**. `/refactor`
returned NO ACTION, so the unit produced **one** commit, not two.

| Artifact | What |
|---|---|
| `backend-3.1-gated-red-usecase-wu3-timings.jsonl` | hook records, WU3 window (44) |
| `backend-3.1-gated-red-usecase-wu3-progress.txt` | per-milestone stamped progress log |

## Where the 1016s went

| Phase | Wall | Share |
|---|---|---|
| Preamble — read progress, ADR, stamp, clear log | 24.4s | 2% |
| **red-agent** | 241.5s | 24% |
| Gap: red return → gate dispatch | 15.7s | 2% |
| **`/test-review`** (detector fan-out 135.5s wall / 335.4s work; serial fixes + verify 172.1s) | 344.6s | 34% |
| Behavior commit (progress advance + commit) | 45.0s | 4% |
| **Refactor batch** — refactor 284.9s ‖ agent-review 319.1s ‖ premortem 325.8s | 336.2s | 33% |
| Tail | 8.4s | 1% |

## The finding this unit produced: batching the review passes is nearly free

The three-way batch (`/refactor` + both passes, one message) ran **929.8s of agent work in
336.2s wall — 2.8×**. The batch wall is set by premortem (325.8s), not by `/refactor`
(284.9s), so the two review passes cost a **marginal 51.3s** over running `/refactor` alone.

That is the number T6 needs. WU2 paid the passes as a **pure 338.8s serial tail** (a design
unit has no `/refactor` to hide them behind); here the same two passes cost 51.3s. The
passes are not expensive — they are expensive *when nothing overlaps them*. The lever is the
batch, not the passes.

## WU2's dispatch-stagger finding, corroborated

Three fan-outs this unit, all with **short** prompts:

| Fan-out | Agents | Stagger |
|---|---|---|
| `/test-review` detectors | 3 | ~5.8s |
| `/refactor` detectors | 3 | ~7.5s |
| Refactor batch | 3 | ~5.2s |

WU2's hazard fan-out staggered **13.5s** per agent — its prompts inlined the full drafted
design plus current code state. These prompts carry a path and a few lines. The 2–2.6×
spread lines up with the prompt-length hypothesis: **the serial prefix is the cost of
emitting each prompt.** Writing the artifact to a file and passing the path should shrink
it. Still worth testing against T3, now with two points instead of one.

## The gate is the single most expensive phase

`/test-review` at 344.6s (34%) beats red-agent at 241.5s. Half of it is **not** the fan-out:
detectors return at 15:03:02 and the gate spends **172.1s** applying 3 fixes serially and
re-verifying. Nested depth-2 trees (gate → 3 detectors, refactor → 3 detectors) mean 8 of
the unit's 10 agents are second-level.

## Verdicts (both non-gating; commit `dec1457` stands)

- **agent-review: CONCERNS (2).** (1) HEAD is an **unbootable application** — `CreateTaskUseCase`
  is `@Service` and now needs `TaskStorage` + `Clock` beans that no adapter supplies, so four
  currently-green acceptance tests would fail; **and the justification I wrote into the commit
  message is factually wrong** — `BoardStorage` and `H2BoardStorage` landed in the *same*
  commit (`dd4b036`), so the "same rhythm 1.1 used" precedent does not exist. (2) The 3.1
  usecase test pins **none** of the write path: `return new Task(randomUUID(), title, desc, 0,
  clock.instant())` passes it with `boardStorage`, `taskStorage` and `addToToDo` all untouched.
  The commit's own mitigation is wrong too — 4.1 cannot catch a missing `save` either, because
  `FakeBoardStorage.getBoard()` returns the same in-memory `Board` that `addToToDo` mutates.
- **premortem: CONCERNS (1 credible).** The **S3 read-after-write guard has no phase left to
  land in** — the same orphan WU2's passes found, now permanent. It was named in the design
  *commit message* but never written into the ADR, so no downstream phase reads it; 4.1's
  Gherkin says "contains task X", not "all five fields identical". Tests go read-only from
  green-usecase, so the window closed with `dec1457`.

## Carry-over

- **Nothing was fixed.** Measurement run; the passes are non-gating. A real run would owe a
  decision on the unbootable context and the orphaned S3 guard before `green-usecase`.
- The S3 orphan is now **two-for-two**: WU2's passes named it, WU3's premortem found it
  again and identified *why* it went missing — a guard recorded only in a commit message is
  invisible to every downstream phase.
- No backend was started; `red-usecase` runs hand-wired unit tests. WU3 is **not**
  contaminated by the Task 18 Step 0 breakage — another clean number.
- The per-milestone stamp addendum held, with one miss: `/test-review` appended its
  "detector fan-out dispatched" stamp retroactively and said so. The hook records cover it.
