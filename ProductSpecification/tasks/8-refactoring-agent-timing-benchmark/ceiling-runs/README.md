# Ceiling baseline runs (Task 8, Step 4 / D4)

Raw measurement artifacts. The distilled per-step time-budget table lands in
[framework-speedup/README.md](../../../framework-speedup/README.md) (Step 4's
third checkbox) — these files are its auditable source.

> **Start here:** [`backend-3.1-gated-run-summary.md`](backend-3.1-gated-run-summary.md) —
> the gated arm's whole-scenario numbers. The spine is **17.4%** of all agent work; the
> dispatch stagger tracks **caller context size**, not prompt length (this retargets T3);
> the review passes' marginal cost is settled at n=6. The per-unit files below are its
> auditable decomposition.

| File | What |
|---|---|
| [`backend-3.1-gated-time-chart.html`](backend-3.1-gated-time-chart.html) | **the gated arm, rendered** — per-unit walls, the four-layer split of agent work, the n=6 overlap rule, and a **projection** of what Tasks 9–18 take off this scenario. Derived from the files below; open it in a browser. The projection is arithmetic on the measured walls, **not a run** — the new-arm ceiling run is what settles it |
| `backend-3.1-old-timings.jsonl` | hook-emitted timing records, run window only (72 records at/after RUN_START) |
| `backend-3.1-old-report.txt` | output of `infrastructure/scripts/agent-timing-report.sh` over that log |
| [**`backend-3.1-gated-run-summary.md`**](backend-3.1-gated-run-summary.md) | **the gated arm, whole scenario** — run-level numbers + the three run-scale findings |
| `backend-3.1-gated-run-timings.jsonl` | hook records, whole gated run (385, 13:08:00Z→17:30:14Z) |
| `backend-3.1-gated-run-report.txt` | `agent-timing-report.sh` over that log — 27 steps, exit 0 |
| [`backend-3.1-gated-red-acceptance.md`](backend-3.1-gated-red-acceptance.md) | **the gated re-run's first work unit, n=2** — red-acceptance under revised D4/D9 (gates included). Start here for the gate numbers. |
| `backend-3.1-gated-red-wu1-timings.jsonl` | hook records, gated WU1 window |
| `backend-3.1-gated-red-rerun-timings.jsonl` | hook records, gated re-run window |
| `backend-3.1-gated-red-rerun-progress.txt` | the re-run's per-milestone stamped progress log — the file that opened the gate tail (kept as `.txt`; `*.log` is gitignored) |
| [`backend-3.1-gated-design.md`](backend-3.1-gated-design.md) | **WU2 `design`, n=1** — 1123s. Where the hazard fan-out + synthesis + review passes go. |
| `backend-3.1-gated-design-wu2-timings.jsonl` | hook records, WU2 window |
| `backend-3.1-gated-design-wu2-progress.txt` | WU2's per-milestone stamped progress log |
| [`backend-3.1-gated-red-usecase.md`](backend-3.1-gated-red-usecase.md) | **WU3 `red-usecase`, n=1** — 1016s. Where `/test-review` and the refactor batch go; shows the review passes cost 51.3s marginal when batched. |
| `backend-3.1-gated-red-usecase-wu3-timings.jsonl` | hook records, WU3 window |
| `backend-3.1-gated-red-usecase-wu3-progress.txt` | WU3's per-milestone stamped progress log |
| [`backend-3.1-gated-green-usecase.md`](backend-3.1-gated-green-usecase.md) | **WU4 `green-usecase`, n=1** — 796s. The spine is a minority of its own unit (green 128.0s < coverage 161.7s); review passes cost a marginal **10.4s** when batched behind a working `/refactor`. |
| `backend-3.1-gated-green-usecase-wu4-timings.jsonl` | hook records, WU4 window |
| `backend-3.1-gated-green-usecase-wu4-progress.txt` | WU4's per-milestone stamped progress log |
| [`backend-3.1-gated-red-usecase-coverage.md`](backend-3.1-gated-red-usecase-coverage.md) | **WU5 `red-usecase` (coverage), n=1** — 897s. The gate is 1.7× the agent it audits; passes cost **0s** marginal (refactor returned last). |
| `backend-3.1-gated-red-usecase-coverage-wu5-timings.jsonl` + `-progress.txt` | hook records (44) + stamped log, WU5 window |
| [`backend-3.1-gated-adapters-discovery.md`](backend-3.1-gated-adapters-discovery.md) | **WU7 `adapters-discovery`, n=1** — **85s, zero agents.** The cheapest unit by 9×; it scheduled 48% of the run's remaining cost. No JSONL: nothing dispatched. |
| [`backend-3.1-gated-red-adapter-storage.md`](backend-3.1-gated-red-adapter-storage.md) | **WU8 `red-adapter storage`, n=1** — 1185s. Both passes convergently proved the storage test **never reads the DB**, after the gate returned 0 findings. |
| `backend-3.1-gated-red-adapter-storage-wu8-timings.jsonl` + `-progress.txt` | hook records (48) + stamped log, WU8 window |
| [`backend-3.1-gated-green-adapter-storage.md`](backend-3.1-gated-green-adapter-storage.md) | **WU9 `green-adapter storage`, n=1** — 841s. The **cheapest agent of the run** (green, 103.2s) wrote the production code; first severity split; the run's only PASS. |
| `backend-3.1-gated-green-adapter-storage-wu9-timings.jsonl` + `-progress.txt` | hook records (32) + stamped log, WU9 window |
| [`backend-3.1-gated-red-adapter-rest.md`](backend-3.1-gated-red-adapter-rest.md) | **WU10 `red-adapter rest`, n=1** — 1068s. Both passes found contract divergence **outside the code they were given** (wire shape; api-spec). |
| `backend-3.1-gated-red-adapter-rest-wu10-timings.jsonl` + `-progress.txt` | hook records (48) + stamped log, WU10 window |
| [`backend-3.1-gated-green-adapter-rest.md`](backend-3.1-gated-green-adapter-rest.md) | **WU11 `green-adapter rest`, n=1** — 1250s ⚠ **window contaminated by a compaction** (~484s); decompacted ≈766s. Carries the stagger natural experiment. |
| `backend-3.1-gated-green-adapter-rest-wu11-timings.jsonl` + `-progress.txt` | hook records (34) + stamped log, WU11 window |
| [`backend-3.1-gated-green-acceptance.md`](backend-3.1-gated-green-acceptance.md) | **WU12 `green-acceptance`, n=1** — 783s. **Scenario complete, 5/5 green.** Second pure serial tail (314.2s); the run's only BLOCK; the disclosed `Clock` rule deviation. |
| `backend-3.1-gated-green-acceptance-wu12-timings.jsonl` + `-progress.txt` | hook records (10) + stamped log, WU12 window |

### The frontend arm (UI 1.1) — STOPPED at 6 of 8 work units (WU7 blocked)

**The arm stopped, and the blocker is the finding.** `green-selenium` (WU7) cannot
run: `infrastructure/.env` is committed with `REPO_INDEX=2`, so the index is
per-**repo**, not per-**session**, and every worktree resolves to the same
8082/5175. A parallel session held 8082 with a **non-empty** board, so the run
would have asserted against another session's data and reported a **false RED**.
CLAUDE.md predicted exactly this: until the `.env`-resolution mechanism lands,
*"the cap is zero — no service-touching unit may run in a worktree at all."*
WU7 stays `[~]`; WU8 `demo` never started.

| File | What |
|---|---|
| [**`frontend-ui1.1-gated-wu2-wu7.md`**](frontend-ui1.1-gated-wu2-wu7.md) | **WU2–WU7, n=6** — 4034.5s wall, 6745.0s agent work, 40 agents, **0 untracked**. **The D4 headline replicates across scenario types: spine 17.1% vs the backend's 17.4%** (WU1's 22.7% was a single-red-unit artifact). **The strongest T3 evidence yet, intra-session**: the orchestrator's stagger **doubled 7.0→15.0s** as its context grew while fresh sub-agent fan-outs stayed flat at 6–10s. **Corrects the review-pass marginal rule** — it is not "has a `/refactor` step" but `max(0, longest_pass − refactor_end)`. Carries the **blocker**, two **instrumentation defects in Task 8's own hooks**, and the **"silent success" defect class (n=5)**. |
| `frontend-ui1.1-gated-wu2-wu7-timings.jsonl` | hook records (187), WU2–WU7 window |
| `frontend-ui1.1-gated-wu2-wu7-report.txt` | reduction over that log — per-agent, per-layer, stagger |
| [**`frontend-ui1.1-gated-red-selenium.md`**](frontend-ui1.1-gated-red-selenium.md) | **WU1 `red-selenium`, n=1** — 1130s. **Carries the T3 test, and it confirms the retargeted hypothesis**: long prompts from a freshly-compacted (small) context staggered **7.0s/agent** — the small-context band, not the long-prompt band — at **position 1 of the run**, which deconfounds the run-position caveat the backend arm's evidence carried. Spine 22.7%; passes 0s marginal (7th point). Three convergences in one unit. The **column-language contradiction** is the frontend arm's `Clock`. |
| `frontend-ui1.1-gated-red-selenium-wu1-timings.jsonl` | hook records (48), WU1 window |
| `frontend-ui1.1-gated-red-selenium-wu1-report.txt` | `agent-timing-report.sh` over that log — 3 steps, exit 0 |
| `frontend-ui1.1-gated-red-selenium-wu1-progress.txt` | WU1's per-milestone stamped progress log |
| `frontend-ui1.1-gated-run-stamps.txt` | the run's orchestrator-level stamp log (RUN_START, per-phase boundaries) |

The run window is copied here rather than referenced, so the artifact stays stable
even as `infrastructure/timings/` churns per session.

**Two runs live here, and they are not the same experiment.** `backend-3.1-old-*` is
the original D4 run: **no gates**, spine only. `backend-3.1-gated-*` is the revised
D4/D9 run: **gates included**, which is what D9 says the benchmark must measure. Do not
compare a number from one against a number from the other.

## Protocol — the gated "old implementation" backend run (2026-07-16)

**This is the arm the new-implementation run must be compared against.** Reproduce it
exactly, or the comparison is not a comparison.

- **Fixture**: `benchmark/pre-3.1` @ `63ef226`. Run branch:
  `benchmark/ceiling-old-3.1-gated`, 16 commits, `17f97b4`..`0928513`.
- **Scenario**: story 1, backend **3.1 Create task with title only**, red-acceptance →
  green-acceptance. **12 work units** (11 planned; +2 inserted by WU4's coverage gate; −1
  skipped at WU6; WU7 inserted 4 concrete adapter steps and skipped 1 check).
- **Included**: everything. `/test-review` and `/test-coverage` gates, `/refactor`, and
  both review passes (`agent-review` + `premortem`) dispatched concurrently with
  `/refactor` in one batch over the immutable behavior commit.
- **Excluded**: human wall-clock only. Design options were auto-approved (zero-latency
  human boundary); the `/continue` invocation structure was preserved per unit.
- **Do-not-fix protocol**: no review-pass finding was ever acted on. The passes are
  non-gating; every verdict was recorded and the commit landed regardless. **One
  deliberate exception**, disclosed in `0928513`: the `Clock` bean (see below).
- Instrumentation on throughout; the fixture carries the Step 1–2 hooks per D8.

### Three constraints the new arm inherits

1. **`Clock` must be handled identically or the comparison is void.** The gated arm's
   scenario could not complete without a one-line `Clock.systemUTC()` bean that no step in
   3.1 has a home for. The spine-only arm never hit this **because its red-agent wrote the
   bean during a RED phase** (`ea4927d`) — a discipline violation its missing gates never
   caught. Both arms paid the cost out of contract; only the gated arm reported it. See
   [`backend-3.1-gated-green-acceptance.md`](backend-3.1-gated-green-acceptance.md).
2. **Count invocations, never assume them.** The old arm's count is an *output* of the
   gates and moved in both directions (11 → 13 → 12).
3. **`stories.md` was never touched by either arm.** Keep it that way; updating it in one
   arm only would add work the other never paid for.

## Protocol — the "old implementation" backend run (2026-07-16)

Reproduce this EXACTLY for the "new implementation" run after Tasks 9–12 land, or
the comparison is not a comparison.

- **Fixture**: `benchmark/pre-3.1` @ `63ef226` (= `f1e361a` + the timing-hook graft).
  Run branch: `benchmark/ceiling-old-3.1`, 10 commits, `f0bef5e`..`6c5a0ad`.
- **Scenario**: story 1, backend **3.1 Create task with title only**, red-acceptance → green-acceptance.
- **Window**: RUN_START `2026-07-16T05:57:59Z` → RUN_END `2026-07-16T06:49:28Z` (**3089.6s wall**).
- **Included in the measured pipeline**: red-acceptance; design (with the full
  8-group hazard fan-out); red/green-usecase; `/test-coverage` after every green;
  adapters-discovery; both adapter red/green pairs (storage, rest); green-acceptance
  (inline, no subagent).
- **Excluded — "no-review", human factor excluded (D4)**: `/test-review`, `/refactor`,
  `agent-review`, `premortem`, and every human approval pause (the design option was
  auto-approved). Their cost is measured separately from real work units (checklist N9)
  and is T6's target, not the spine T2/T3/T5 restructure.
- Instrumentation on throughout; the fixture carries the Step 1–2 hooks per D8.

## Known limitation applying to these numbers

Per `progress-hooks.md`: `SubagentStop` fires for spawn paths that emit no
`SubagentStart`, so busy totals under-count untracked spawns.

**The spine-only run: the 2 untracked stops both predate RUN_START** (05:46, 05:48) and
fall outside the window — so its step table is not affected by them.

**The gated run, re-checked rather than inherited: 6 untracked stops fall inside the
window** — 2 each in the WU5, WU11 and WU12 windows. Busy totals for those three units
under-count accordingly. This cuts *against* the spine, not for it: the run-summary's
**17.4% spine share** is computed from tracked agents only, so the true share is if
anything lower. The gated run also carries **4 unclosed `subagent_start` records**
(13:25:38–13:26:08Z — a `refactor-agent` and its three detectors, dispatched after WU1's
re-run commit and abandoned). They contribute no duration and the report flags rather than
sums them, so no published number moves.

## Environment notes (cost you don't see in the table)

The backend must be running for the acceptance layer; `green-acceptance` also
requires a jar rebuild + restart (liquibase `002` applies there). That work is
orchestrator time, folded into the wall-clock gap between agent steps, not into
any agent's duration.
