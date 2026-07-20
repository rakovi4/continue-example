# red-acceptance 3.1, gated — first measurement (n=2)

The first work unit measured **with gates included** (revised D4/D9). Two runs of the
same work unit from the same fixture commit `63ef226`, branch
`benchmark/ceiling-old-3.1-gated`.

| Artifact | What |
|---|---|
| `backend-3.1-gated-red-wu1-timings.jsonl` | hook records, WU1 window (2026-07-16 10:55–11:25Z) |
| `backend-3.1-gated-red-rerun-timings.jsonl` | hook records, re-run window (from 13:08:00Z) |
| `backend-3.1-gated-red-rerun-progress.txt` | the re-run's **per-milestone stamped** progress log — the file that opened the tail |

WU1's commits are preserved at tags `bench/wu1-red-testreview` (`6f6dce2`) and
`bench/wu1-refactor` (`0e6f46d`). The re-run's red+test-review commit is `17f97b4`.

## The numbers

| Agent | WU1 | Re-run | Spread |
|---|---|---|---|
| `red-agent` | 323.4s | 419.5s | **+96.1s (+30%)** |
| `/test-review` | 549.4s | 536.8s | −12.6s (−2%) |
| `/refactor` | 386.2s | not re-run | — |
| `agent-review` | 209.1s | not re-run | — |
| `premortem` | 178.2s | not re-run | — |

`/refactor` and the passes were dropped from the re-run deliberately: the run existed to
decompose the gate tail, and dispatching the batch concurrently would have contended for
the host and corrupted the `/test-review` comparison.

**n=2 reads:** `/test-review` is stable within 2% — the gate's cost is structural, not
noise, and the per-fix stamp addendum on the dispatch prompt is cheap enough to keep.
`red-agent` swings 30%, because it is **prediction-loop-bound**: the re-run needed two
iterations (predicted RestAssured's content-type-mismatch text, got its no-default-parser
text), WU1 needed one. That variance is inherent to the red protocol, not measurement
noise — an n=2 mean here would be a fiction.

## Where `red-agent`'s 419.5s went

From the stamped log (local +03:00; hook start 16:08:19.9):

| Phase | Wall |
|---|---|
| Preamble → `START` | 12s |
| Context read (spec, api-spec, Statements, DTOs, backend state) | **141s** |
| Start backend | 13s |
| Write test + state prediction | 31s |
| **`bootRun` came up on 8080 — `BACKEND_PORT` not exported; killed own PID, restarted** | **86s** |
| Backend UP on 8086 + probe | 28s |
| Iteration 1 run (prediction mismatch) | 35s |
| Iteration 2 run + full suite + `@Disabled` | 42s |
| Wrap | 31s |

**Backend startup cost: 145s** (16:11:06 → 16:13:31), of which **86s was recovering from a
port misconfiguration** — `run-backend` does not export `BACKEND_PORT`, so the app bound
8080 and the agent had to kill it and start over. That is Task 18 Step 0 material, found
independently of the script bugs `/test-review` reported.

## What this unit proves for Task 18

The three-cold-starts thesis is not inferred — it is in the logs:

1. `red-agent` starts a backend (145s), and **kills it at the end** (its report: "Backend
   on 8086 has been stopped").
2. `/test-review` therefore starts another — `test-runner` spent **~147s before logging
   `RUN`** and ~40s executing.
3. `/refactor` would have started a third (WU1's 213.1s tail contains a full acceptance
   run).

~145s × 3 ≈ 435s per work unit spent standing up the same stack, torn down each time by
the agent that built it. Two independent measurements (red-agent's 145s, test-runner's
147s) agree on what one cold start costs.

## Carry-over for the remaining ten units

- The gate dispatch prompts must keep the **per-milestone stamp addendum** (stamp at the
  moment, from `date +%H:%M:%S`, never batched at the end). Without it the tail is opaque
  — that blind spot cost three wrong mechanisms (see checklist N16→N18).
- `red-agent` ran `pkill -f "com.example.Application"` — a **kill-by-name, forbidden** by
  `.claude/rules/infrastructure.md` because it hits other sessions' backends. It
  self-reported; nothing else was running, so no collateral. The rule is in the agent's
  always-on context and it broke it anyway, which is a fact about the agent, not this run.
- `/test-acceptance` is unusable on this Linux host (`JAVA_HOME` hardcoded to a Windows
  path, `gradlew` committed `100644`), so both runs measure agents improvising around it.
  **Every timing here is a workaround's timing.** Task 18 Step 0 fixes that; re-measure
  after it lands rather than treating these as the floor.
