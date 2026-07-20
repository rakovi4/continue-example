# Backend 3.1, gated ceiling run — whole-scenario summary (D4/D9)

Scenario 1/3.1 "Create task with title only", **gates included**, run to completion on
branch `benchmark/ceiling-old-3.1-gated` from fixture `benchmark/pre-3.1` @ `63ef226`.
16 commits, `17f97b4`..`0928513`. Acceptance suite **5 passed, 0 failed, 0 skipped**.

This is the file to read first. Per-unit decompositions are the `backend-3.1-gated-*.md`
siblings; this one carries the run-level numbers and the three findings that only exist
at run scale.

| Artifact | What |
|---|---|
| `backend-3.1-gated-run-timings.jsonl` | hook records, whole gated run (385 records, 13:08:00Z→17:30:14Z) |
| `backend-3.1-gated-run-report.txt` | `agent-timing-report.sh` over that log — 27 steps, exit 0 |

## The headline: the spine is 17.4% of the work

Across the whole scenario, 89 tracked agents ran **17402.5s** of agent work:

| Layer | Busy | Share |
|---|---|---|
| **Spine** — `red-agent` + `green-agent` + `hazard-scan-agent` (design) | 3029.4s | **17.4%** |
| Gates — `/test-review` + `/test-coverage` (incl. their detectors) | 4224.0s | 24.3% |
| Review passes — `agent-review` + `premortem` | 4838.8s | 27.8% |
| `/refactor` (incl. its detectors) | 5007.2s | 28.8% |

`green-agent` — the agent that writes the production code, three times over — is
**385.1s, 2.2%** of the run. The two review passes alone cost **12.6× more** than every
green phase combined. WU4 called the spine "a minority of its own work unit"; at run
scale it is a minority by a factor of five.

**This is the number D4 exists to produce.** T2/T3/T5 restructure the spine. The spine is
not where the time is.

## Per-unit walls

Window = first agent dispatch → the unit's final commit (or, for a NO-ACTION `/refactor`,
the batch's last return). Human wall-clock excluded per D4; the orchestrator preamble
(~20–30s, measured in WU1–WU4) falls outside this definition and is excluded here, so
these are ~25s narrower than WU2–WU4's published windows on a like-for-like basis.

| Unit | Step | Wall | Commits |
|---|---|---|---|
| WU2 | `design` | 1123s | 1 |
| WU3 | `red-usecase` | 1016s | 1 (refactor NO ACTION) |
| WU4 | `green-usecase` | 796s | 2 |
| WU5 | `red-usecase` (coverage) | 897s | 1 (refactor NO ACTION) |
| WU6 | `green-usecase` (coverage) | — | `[S]` skipped |
| WU7 | `adapters-discovery` | **85s** | 1 (progress-only) |
| WU8 | `red-adapter storage` | 1185s | 2 |
| WU9 | `green-adapter storage` | 841s | 2 |
| WU10 | `red-adapter rest` | 1068s | 2 |
| WU11 | `green-adapter rest` | 1250s ⚠ | 2 |
| WU12 | `green-acceptance` | 783s | 1 |
| | **WU2–WU12 total** | **9044s (2h30m)** | |

⚠ **WU11's number is not clean.** Its window spans a conversation compaction: a 549s gap
sits between the coverage agent's return and the refactor batch's dispatch, of which
~484s is compaction, not work. Decompacted, WU11 ≈ **766s** and the WU2–WU12 total ≈
**8560s (2h23m)**. Both figures are given; neither is quietly averaged in.

**WU1 is excluded** from the total. It was measured n=2 with deliberately different
composition (the re-run dropped `/refactor` and the passes to isolate the gate), so it
has no single comparable unit wall. Its decomposition is in
[`backend-3.1-gated-red-acceptance.md`](backend-3.1-gated-red-acceptance.md). Estimating
its full-composition wall at ~1400s puts the complete gated scenario near **~9960s
(~2h46m)**, against the spine-only run's **3089.6s (51m30s)** — roughly **3.2×**, or
**~2.95× per work unit** (830s vs 281s). Treat the 3.2× as an estimate resting on that
one reconstructed unit, not as a measured figure.

## Finding 1: the dispatch stagger tracks the *caller's context size*, not prompt length

WU2→WU4 built a four-point case that the serial prefix on a fan-out is the cost of
*emitting each prompt*, scaling with prompt length. **The compaction inside WU11 is an
accidental controlled experiment, and it does not support that.**

Same orchestrator, same dispatch machinery, same long-prompt style, minutes apart —
only the context size changed:

| Fan-out (orchestrator-dispatched, one message) | Stagger/agent | Context |
|---|---|---|
| WU5 refactor batch | 13.0s | pre-compaction |
| WU8 refactor batch | 14.0s | pre-compaction |
| WU9 refactor batch | 14.0s | pre-compaction |
| WU10 refactor batch | 15.0s | pre-compaction |
| **WU11 refactor batch** | **8.0s** | **post-compaction** |
| **WU12 review passes** | **9.0s** | **post-compaction** |

And every fan-out dispatched *by a sub-agent* — which always has a small, fresh context —
sits in the same band the compacted orchestrator dropped into:

| Sub-agent fan-out | Stagger/agent |
|---|---|
| WU5 `/test-review` detectors | 7.0s |
| WU5 `/refactor` detectors | 5.5s |
| WU8 `/test-review` detectors | 9.5s |
| WU8 `/refactor` detectors | 9.0s |
| WU9 `/refactor` detectors | 9.0s |
| WU10 `/test-review` detectors | 9.5s |
| WU10 `/refactor` detectors | 6.5s |
| WU11 `/refactor` detectors | 7.5s |

Orchestrator pre-compaction: **13.0–15.0s** (mean 14.0). Orchestrator post-compaction:
**8.0–9.0s**. Sub-agents, always: **5.5–9.5s** (mean 7.9). Compacting the orchestrator
moved it into the sub-agent band.

WU4's "controlled comparison" (orchestrator long prompts 11.4s vs `/refactor` short
prompts 6.3s, same unit, minutes apart) was **confounded**: the orchestrator has both a
large context and long prompts; the refactor-agent has both a small context and short
prompts. The compaction separates the two variables, and context size is the one that
moved. WU11's batch prompts were *long* — the known-findings list inlined into all three —
and still staggered 8.0s.

**This retargets T3.** T3 proposes writing the artifact to a file and passing a path — a
prompt-length fix. If the dominant term is caller context size, T3 addresses the smaller
variable. It may still help indirectly (an artifact not inlined never enters the
transcript, so context grows slower), but the lever it was designed around is not the one
the data points at.

**Caveats, stated plainly.** n=2 post-compaction. The post-compaction units are also the
*latest* units in the run, so host state and caching are not excluded. This is a
hypothesis with a natural experiment behind it, not a measured result — it needs the
controlled test T3 was always owed, now re-scoped to vary context size rather than prompt
length.

## Finding 2: the review passes cost whatever premortem overruns `/refactor` by — confirmed at n=6

Six data points now, and the rule holds exactly:

| Unit | `/refactor` | Passes' marginal cost | Why |
|---|---|---|---|
| WU2 `design` | none | **338.8s** | pure serial tail — nothing to hide behind |
| WU3 `red-usecase` | 284.9s (NO ACTION) | 51.3s | barely outlasts premortem |
| WU4 `green-usecase` | 384.7s | 10.4s | long enough to swallow both |
| WU5 `red-usecase` (cov) | 254.8s (NO ACTION) | **0s** | refactor returned *last* |
| WU8 `red-adapter storage` | 328.0s | 102.8s | premortem (402.5s) overran it |
| WU9 `green-adapter storage` | 487.0s | **0s** | refactor returned last |
| WU12 `green-acceptance` | none | **314.2s** | pure serial tail — second WU2 |

WU12 is the finding's confirmation. `green-acceptance` has no `/refactor` step, so its
passes ran as a pure serial tail and cost **314.2s** — within 8% of WU2's 338.8s, the
only other unit with nothing to overlap. Two units with no `/refactor`: ~330s. Five units
with one: 0–103s.

**T6's lever is the batch, settled.** The passes are not expensive; they are expensive
*when nothing overlaps them*. The two step types that produce no `/refactor` —
`design` and `green-acceptance` — are the only two that pay full price.

## Finding 3: the invocation count is not fixed in advance, and moves in both directions

D4 assumed a fixed 11 `/continue` invocations for the old arm. The run delivered 12:

- 3.1 began with 11 planned steps.
- WU4's coverage gate **inserted 2** (`red`/`green-usecase` for `ColumnNotFoundException`) → 13.
- WU6 was immediately **skipped `[S]`** — the guard had already landed with WU4 → 12.
- WU7's `adapters-discovery` **inserted 4** concrete adapter steps (2 red, 2 green) and
  **skipped 1** check as unreachable.

The old arm's invocation count is an *output* of the gates, not an input to the benchmark.
Any new-arm comparison must count invocations rather than assume them.

**WU7 is the cheapest unit of the run by an order of magnitude: 85s, zero agents.** A gate
that restructured the remaining half of the scenario cost 1.4 minutes and dispatched
nothing. Not every gate is expensive; the expensive ones are the ones that spawn fleets.

## Data-quality notes

- **Untracked stops: 6** across the run (2 each in the WU5, WU11 and WU12 windows) — the
  `progress-hooks.md` known limitation (`SubagentStop` fires for spawn paths emitting no
  `SubagentStart`). Busy totals therefore **under-count**, so the 17.4% spine share is if
  anything an over-estimate of the spine.
- **4 unclosed `subagent_start` records** at 13:25:38–13:26:08Z: a `refactor-agent` plus
  its three detectors, dispatched after WU1's re-run commit and abandoned. WU1's write-up
  says `/refactor` was "not re-run", which is true of its *result* but not of its
  dispatch — the agents started and were never stopped. They contribute no duration (the
  report flags them rather than summing them), so no published number moves; recorded so
  the log's shape is not mistaken for corruption.
- `agent-timing-report.sh` exits 0 on this log: no orphan stops of tracked agents, no
  empty-`agent_id` lifecycle records, no `agent_type` contradictions.
- **A pipe truncated this run's own report file once.** `report.sh … | tee f | head -40`
  sent SIGPIPE and left `f` cut at 69 of 181 lines — the same `| tail`/`| head` masking
  that WU4 flagged on the gradle wrapper, reproduced on the measurement tooling itself.
  Regenerated with a plain redirect.
