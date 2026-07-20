# adapters-discovery 3.1, gated — WU7 measurement (n=1)

Seventh work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commit
`960e222`. Window **2026-07-16T16:00:50Z → 16:02:15Z = 85s**. Progress-only commit, so
per `/continue`'s rules no `/refactor` and no review passes ran.

**No timings JSONL artifact exists for this unit, and that is the finding.** The window
contains **zero** hook records because the unit dispatched **zero agents**.

## The finding: the cheapest unit of the run by an order of magnitude

| | WU7 | Next cheapest (WU12) | Run mean (WU2–WU12) |
|---|---|---|---|
| Wall | **85s** | 783s | 904s |
| Agents | **0** | 2 | 8.3 |

WU7 is **9.2× cheaper than the next cheapest unit** and **10.6× cheaper than the run
mean**.

It is a gate — the same category of step as `/test-review` (359.4s mean across the run's
four red units) and `/test-coverage` (161.3s mean across three greens) — and it costs 1.4
minutes of orchestrator reading.

**Not every gate is expensive. The expensive ones are the ones that spawn fleets.** A gate
whose work is "read the usecase constructor and write down what you find" costs
essentially nothing. This matters for T6: "gates are the cost" is too coarse a claim. The
cost is *sub-agent fan-out*, and gates are merely where most of the fan-out lives.

## What it decided

Three checks from `.claude/templates/workflow/adapter-discovery-checklist.md`:

| Check | Outcome |
|---|---|
| 1 — ports | `TaskStorage` unimplemented → **storage** adapter needed |
| 2 — exceptions | `ColumnNotFoundException` unreachable via REST → **`[S]` skipped** |
| 3 — response shape | controller returns `void`, scenario expects a `TaskResponse` body → **rest** adapter needed |

It **inserted 4 concrete steps** (`red`/`green-adapter storage`, `red`/`green-adapter
rest`) and **skipped 1 check**. Those four steps became WU8–WU11 and account for
**4344s — 48% of the WU2–WU12 total**.

An 85-second unit scheduled roughly half the scenario's remaining cost. Its check-2 skip
was also vindicated at run scale: WU12's `agent-review` independently re-derived that
`ColumnNotFoundException` is unreachable (`H2BoardStorage.buildColumns` iterates
`ColumnType.values()`, so all three columns always exist) — the gate's call was right,
made in seconds, from the constructor alone.

## D4 datum: the invocation count is an output, not an input

WU7 is half of why the old arm ran 12 work units rather than D4's assumed 11:

- 11 planned → WU4's coverage gate **inserted 2** → 13
- WU6 immediately **`[S]`** (guard already landed) → 12
- WU7 **inserted 4** concrete steps and **skipped 1** check

The count moves in both directions and is not knowable before the gates run. Any new-arm
comparison must **count** invocations, never assume them.

## Carry-over

- No agents, no test run, no production or test file touched — a pure progress-file commit.
- No backend was started; **not** contaminated by the Task 18 Step 0 breakage.
- The stamped-log addendum is not applicable: nothing dispatched, nothing to stamp.
