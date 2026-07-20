# green-adapter rest 3.1, gated — WU11 measurement (n=1, window contaminated)

Eleventh work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commits
`3f9162e` (behavior) + `bd9b493` (refactor). Window **2026-07-16T16:56:13Z → 17:17:03Z =
1250s (20m50s)**.

> ⚠ **This unit's wall-clock is not a clean measurement.** A conversation compaction sits
> inside the window: 549s separate the coverage agent's return (17:02:00) from the refactor
> batch's dispatch (17:11:09), of which ~484s is compaction rather than work. Decompacted,
> WU11 ≈ **766s**. The per-agent numbers below are unaffected — only the window total is.
> Recorded rather than silently averaged into the run.

| Artifact | What |
|---|---|
| `backend-3.1-gated-green-adapter-rest-wu11-timings.jsonl` | hook records, WU11 window (34) |
| `backend-3.1-gated-green-adapter-rest-wu11-progress.txt` | per-milestone stamped progress log |

**Untracked stops: 2** in this window — busy totals under-count by that much.

## Where the 1250s went

| Phase | Wall | Share |
|---|---|---|
| **green-agent** | 153.9s | 12% |
| Gap: green return → coverage dispatch | 25s | 2% |
| **`/test-coverage rest --focus`** | 168.7s | 13% |
| **Compaction + behavior commit** | **549s** | **44%** ⚠ |
| **Refactor batch** — refactor 269.5s ‖ agent-review 158.6s ‖ premortem 324.6s | 341s | 27% |
| Tail (refactor commit) | 13s | 1% |

Decompacted, the shape matches WU9 closely: green 12%, coverage 13%, batch ~45%.

## The spine holds at ~12%

**green-agent: 153.9s, GREEN on the first run.** It created `TaskResponseDto` (27 lines),
changed `TaskController` to return it, and changed `ColumnResponseDto.tasks` from
`List<Object>` to `List<TaskResponseDto>`. It is **9% cheaper than the coverage pass
auditing it** (168.7s) — the same inversion as WU4 (128.0s vs 161.7s) and WU9 (103.2s vs
153.6s). Three green units, three times the audit outweighed the implementation.

## The deliberate call: a finding actioned by prompt, not by authority

The `List<Object>` → `List<TaskResponseDto>` change is the one place in the run where a
review-pass finding changed later code. WU10's `agent-review` found it; **minimal-
implementation TDD would not have forced it** (every board fixture has an empty task list,
so no test can distinguish the two). It landed because the orchestrator carried it forward
in green-agent's prompt — not because the pass had authority. The passes remained
non-gating throughout.

## `/test-coverage`: 0 steps added, and the third executed-vs-pinned instance

The coverage gate declined to insert a step, on two converging grounds: 3.1 has **no
test-writing window at any layer**, and the behavior belongs to 4.1, whose
`adapters-discovery` gate is the sanctioned mechanism. Its findings:

- **`ColumnResponseDto.from` reports 3/3 lines while `.map(TaskResponseDto::from)` is
  applied to zero elements.** The mapping has never run. This is the **third independent
  instance** of the run's executed-vs-pinned pattern (after `CreateTaskUseCase.save` at
  WU4 and the storage test's phantom DB read at WU8).
- The 38 missed branches on `TaskResponseDto` are a **Lombok artifact**, not a regression —
  all in generated `equals`/`hashCode`/`toString`, attributed to line 9, alongside **zero**
  missed lines. `BoardResponseDto` and `ErrorResponseDto` show the identical shape, so it
  is the baseline.
- **Carry-forward: 4.1's rest fixture must carry a non-empty task list**, or the mapping
  stays unpinned.

## A third java-spring tooling defect

`backend/*/src/main/**` **cannot match adapter modules at all**. They sit two segments deep
(`backend/adapters/rest/...`) and `*` does not cross `/`. `backend/**/src/main/**` is
required. This is independent of the two defects WU4 found in the same file
(`coverage-commands.md`'s off-by-one awk columns; the focus filter's blindness to untracked
files) and was confirmed first-hand here. Three defects, one template, all of which would
silently corrupt a focus-mode coverage run.

## Stagger: the compaction's natural experiment

WU11's refactor batch staggered **8.0s/agent** — against 13.0–15.0s for the identical
orchestrator-dispatched batch in WU5, WU8, WU9 and WU10. **The prompts were long** (the
accumulated known-findings list inlined into all three). The only variable that changed is
the orchestrator's context size, halved by the compaction minutes earlier.

This is the datum that retargets T3, which was designed around prompt length. Full analysis
and caveats in [`backend-3.1-gated-run-summary.md`](backend-3.1-gated-run-summary.md)
Finding 1.

## Verdicts (both non-gating; commits stand)

**The passes convergently found the same defect — the fourth convergence of the run.** Both
independently established that changing `ColumnResponseDto.tasks` **silently broke `GET
/board`'s wire contract**, and that the acceptance client's
`dto/board/TaskSummaryResponse` still models the old nested `{"title":{"value":"X"}}` shape.

- **agent-review: CONCERNS (1).** Jackson cannot bind a JSON string to `ValueWrapper`
  (`@Data @NoArgsConstructor`, no String-arg creator), so the first non-empty board throws
  `MismatchedInputException` inside the test client — a crash that reads like a backend
  defect and costs a diagnosis cycle to trace back here.
- **premortem: CONCERNS (3 credible).** Same artifact, worse failure mode:
  `getTitleValue()` returns `null` rather than throwing, so the board renders **blank
  titles, silently**. Plus the `hibernate.jdbc.time_zone` half-ship, and the api-spec
  contradiction escalated from latent to live now that a body exists on the wire.

Both note the commit message's own remediation plan ("4.1's rest fixture must carry a
non-empty task list") closes the **rest** half only; the acceptance client is a second lock
on the same door.

## Carry-over

- **Nothing was fixed.** Measurement run; the passes are non-gating.
- `/refactor` applied one A32 — separating the side-effecting usecase call from the pure
  mapping expression, converging on `BoardController`'s existing shape. Rest module 4/4.
- No backend was started (`@WebMvcTest` slice) — **not** contaminated by Task 18 Step 0.
- The per-milestone stamp addendum held with no misses.
