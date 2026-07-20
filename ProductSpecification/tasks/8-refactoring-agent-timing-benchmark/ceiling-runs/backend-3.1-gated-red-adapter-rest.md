# red-adapter rest 3.1, gated — WU10 measurement (n=1)

Tenth work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commits
`3f9d672` (behavior) + `858e87b` (refactor). Window **2026-07-16T16:37:51Z → 16:55:39Z =
1068s (17m48s)**.

| Artifact | What |
|---|---|
| `backend-3.1-gated-red-adapter-rest-wu10-timings.jsonl` | hook records, WU10 window (48) |
| `backend-3.1-gated-red-adapter-rest-wu10-progress.txt` | per-milestone stamped progress log |

**Untracked stops: 0.** 12 agents, 12 starts, 12 stops.

## Where the 1068s went

| Phase | Wall | Share |
|---|---|---|
| **red-agent** | 203.2s | 19% |
| Gap: red return → gate dispatch | 22s | 2% |
| **`/test-review`** (3 detectors + a `test-runner` verification at 86.1s) | 348.4s | 33% |
| Behavior commit | 24s | 2% |
| Gap: commit → batch dispatch | 21s | 2% |
| **Refactor batch** — refactor 425.9s ‖ agent-review 320.7s ‖ premortem 347.3s | 425.9s | 40% |
| Tail (refactor commit) | 24s | 2% |

`/test-review` (348.4s) is **1.7× red-agent** (203.2s) — the gate outweighs the agent it
audits, as in every red unit of this run.

## The finding: both passes found contract divergence *outside the code they were given*

Neither finding is in the diff. Both passes went looking past their input:

**agent-review — the wire shape.** `ColumnResponseDto.tasks` is `List<Object>` holding raw
domain entities, so `GET /board` would render `"title": {"value": "..."}` nested while
`POST /tasks` renders it flat. Structurally invisible in scenario 1.1 because
`get-empty-board.json` has empty task lists — the mapping is *constructed* but never
*applied*. (WU11 fixed this; WU11's own passes then found the acceptance client still
mirrors the old nested shape.)

**premortem — the API spec.** `api-specs/tasks_create.yaml` declares `id: integer($int64)`
and `created_at` snake_case, contradicting the UUID string + camelCase this commit pins.
Three compounding facts: `int64` was **never buildable** (the ADR chose UUID); **no test
reads an api-spec**; and **no remaining scenario has an `api-spec` step**. The spec and the
code diverged with nothing in the pipeline able to notice, and no phase left to reconcile
them.

Together these are the run's clearest evidence that the passes are not redundant with the
gates. `/test-review` audits the tests against the code. Both of these are the *code
against artifacts outside the test suite* — a wire contract shared with a sibling endpoint,
and a spec file no test reads. Nothing else in the pipeline looks there.

## Batching

The batch ran **1496.4s of agent work in 425.9s wall (3.51×)**. `refactor-agent` returned
last (16:55:15), after premortem (16:54:27) and agent-review (16:53:45) — so the passes
cost a **marginal 0s**. Third zero-marginal unit of the run (WU5, WU9, WU10).

## Stagger: the two fan-outs disagree with the prompt-length hypothesis

WU10 dispatched two fan-outs minutes apart, and — read alone — they look like WU4's
"controlled comparison":

| Fan-out | Dispatched by | Prompt | Stagger |
|---|---|---|---|
| Refactor batch | orchestrator | long | **15.0s** |
| `/refactor` detectors | refactor-agent | short | **6.5s** |

2.3× spread, which WU4 attributed to prompt length. **WU11's compaction shows this reading
is confounded** — the orchestrator has both a large context and long prompts, and it is
the context that moves the number. See
[`backend-3.1-gated-run-summary.md`](backend-3.1-gated-run-summary.md) Finding 1. WU10's
15.0s is the run's **largest** orchestrator stagger, and it came from the run's largest
pre-compaction context.

## `/refactor` extracted `readFixture`

`RestTest` gained `readFixture(String)` — try-with-resources, `Objects.requireNonNull`,
explicit `StandardCharsets.UTF_8` — plus a `var` purge and dead-import removal. This is the
helper WU11 then reused rather than re-extracting.

## Verdicts (both non-gating; commits stand)

- **agent-review: CONCERNS** — the `List<Object>` wire-shape divergence.
- **premortem: CONCERNS** — the api-spec contradiction (`int64` id, snake_case
  `created_at`) with no phase left to reconcile it.

## Carry-over

- **Nothing was fixed.** Measurement run; the passes are non-gating.
- The `List<Object>` finding was **actioned in WU11** — deliberately, since
  minimal-implementation TDD would not have forced it. That is the one place in the run
  where a pass's finding changed later code, and it happened because the orchestrator
  carried it forward in a prompt, not because the pass had authority.
- No backend was started (`@WebMvcTest` slice) — **not** contaminated by Task 18 Step 0.
- The per-milestone stamp addendum held with no misses.
