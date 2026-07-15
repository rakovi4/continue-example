# Framework Speed-Up Initiative

Recover the development speed lost after team adoption of the framework, by parallelizing
the `/continue` work-unit pipeline, making orchestration deterministic, reflecting the
de-facto auto-apply practice for review concerns, and fixing the `/story` skill concept.

- [initial-prompt.md](initial-prompt.md) — problem statement + follow-up clarifications
- [checklist.md](checklist.md) — traceability: every concern → task (or explicit non-task decision)

## Baseline (measured from `../stories.md` history)

| Metric | Author (Feb 20 → Mar 19, 2026) | Team (Jul 2026) | Gap |
|---|---|---|---|
| Test cases done | 131 → 324 (Δ193 in 27 calendar days) | — | — |
| Per calendar day | ~7.1 | — | — |
| Per full 8h workday (est.) | **~10–12** | **~3** | **3–4×** |
| Parallel sessions | 6 | 3–4 | not a target to close |
| Per session-day | ~1.7–2.0 | ~0.86 | **~2×** |

Target: close the ~2× per-session regression AND raise absolute step/scenario/story speed
regardless of session count. Pushing the team to 6 parallel sessions is explicitly NOT a
lever.

## Decisions Reached

| # | Decision | Rationale |
|---|---|---|
| D1 | Orchestration moves to the deterministic **Workflow tool** (JS script with `parallel()`/`pipeline()`), not prose fan-out instructions in `/continue` | Directly answers the "complex context engineering / make it deterministic" concern; parallelism becomes guaranteed, not hoped-for |
| D2 | Workflow billing verified: subagents run in-session, same metering as the Agent tool, counted against subscription usage limits; no separate tariff. Only caveat: faster usage-limit consumption | Question raised re: OpenClaw-style per-agent tariffs — does not apply |
| D3 | Review-pass concerns: **blanket auto-apply + commit, review post-factum** | Already de-facto practice in team copies of the framework; formalize rather than fight it. Fixes land as a separate commit so they remain auditable |
| D4 | Benchmark = **theoretical ceiling only**: no-review red→green-acceptance run on a fixed commit, old vs new implementation, human factor excluded | Fixtures: story 2 first backend scenario; story 1 first UI scenario |
| D5 | `/story` (spec generator) is only ever called from `/continue` → downgrade to prompt template; the `/story` name is freed for a backlog-add skill with a story-quality guardrail | No standalone usage exists, so no muscle-memory break |
| D6 | Human review steps between parallelized red/green pairs (1.5, 1.9) are removed | Required for the parallel pipelines to be pipelines at all |
| D7 | Under Workflow orchestration, sub-agents never run `git commit` — the script is the single committer, landing commits serially at defined points (worktree isolation only when parallel agents must write the same files) | Eliminates the commit race that cross-unit overlap (1.6b) would otherwise create; semantic drift (rename vs freshly written test) is caught by the red prediction-mismatch rule |

## Task Breakdown

Impact figures are hypotheses until Task 1 produces measured numbers.

| Task | Scope | Depends on | Expected impact |
|---|---|---|---|
| T1 Instrumentation + ceiling benchmark | Harness-level timestamps for EVERY agent and subagent run (hook-emitted at dispatch/return, never agent-self-reported — immune to hallucination); per-step time-budget table; ceiling runs (D4) on both fixtures, old vs new | — | Unblocks all estimates |
| T2 Workflow-based deterministic orchestration | Convert `/continue` dispatch sequences to Workflow scripts (D1) | T1 (baseline first) | Enabler for T3–T5 |
| T3 Parallel adapter red+green pipelines (1.5) | `pipeline(adapters, red, green)`; drop inter-pair human review (D6) | T2 | High (~15–20%/backend scenario) |
| T4 Parallel frontend chains (1.9) | Application red-green ∥ client red-green; drop inter-pair review (D6) | T2 | High (frontend scenarios) |
| T5 Coverage-red fan-out + cross-unit overlap (1.6) | N gaps → N parallel red agents, 1 green after; red agents start while original green's refactor+review batch still runs. Commit-race fix (D7): agents don't commit — the Workflow script is the single committer, serializing commits at defined points; residual rename-vs-new-test drift is caught by the red prediction-mismatch rule | T2 | Medium (~5–10%) |
| T6 Review-pass + gate-agent parallelism (1.2, 1.3, 1.4, 1.7, 1.8) | Measure review passes; verify detection-cluster fan-out (refactor M/D/T, test-review A/P/Se/S); diff-snapshot input so passes start before the behavior commit; speed-ups (context size, model/effort tier) | T1 (data) | High if measurement confirms serial tails |
| T7 Auto-apply review concerns (2) | Implement D3: apply + commit review-pass fixes automatically, surfaced for post-factum review | — | Removes a manual round-trip per work unit |
| T8 `/story` split + guardrail (3a–c) | Downgrade spec generator to `/continue`-internal template; new `/story` = backlog-row add; guardrail: story = pain to cure / opportunity to gain — reject tech stories ("add Kafka streaming") and part-stories; includes story-size guardrail (see checklist N1) | — | Fixes misunderstanding; prevents multi-week 100-case stories |

## Open Concerns — Deliberately Not Tasked Yet

| Concern | Status |
|---|---|
| Cross-story parallelism: agents don't help the team keep 3–4 sessions fed with independent work | Needs its own design discussion once T1–T2 land; candidate: `/continue` suggesting parallelizable fronts across stories/tasks |
| "Paranoid bureaucratic" gate perception — which gates earn their cost? | Partially addressed by T6/T7 speed-ups; the deeper gate-pruning question waits for T1's per-gate cost data — prune with numbers, not vibes |
| Review thoroughness is drifting down under speed pressure | Not a speed task; auto-apply (T7) keeps concerns applied instead of ignored, which is the mitigation available now |

## References

- [`.claude/skills/continue/SKILL.md`](../../.claude/skills/continue/SKILL.md) — dispatch tables + pre-commit review passes (primary artifact under change)
- [`.claude/guidelines/workflow-detail.md`](../../.claude/guidelines/workflow-detail.md) — per-phase scenario sequences (T3–T5 change these)
- [`.claude/guidelines/tdd-rules.md`](../../.claude/guidelines/tdd-rules.md) — phase-transition rules the parallel pipelines must still honor
- [`../stories.md`](../stories.md) — source of the baseline speed numbers
