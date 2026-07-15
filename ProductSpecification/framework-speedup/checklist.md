# Traceability Checklist

Every item from [initial-prompt.md](initial-prompt.md) plus every concern surfaced during
the discussion, mapped to a task (T1–T8, see [README.md](README.md)) or an explicit
non-task decision (D1–D6). Tick a row only when the covering task's change has landed
(or, for decision rows, when the decision is reflected in the docs). An unticked row with
no task is a hole — fix the mapping, don't ignore it.

## From the initial prompt

- [ ] **1** Speed regression 10–12 → 3 per workday — quantified in README baseline; addressed by the initiative as a whole (T1–T7)
- [x] **1.1** 6 vs 3–4 parallel sessions — decision: NOT a lever; per-session speed instead (README "Target"). No task by design
- [ ] **1.2a** Measure review-agent execution time → **T1** — timings must be hook-emitted by the harness (dispatch/return timestamps), NEVER agent-self-reported: the footprint must be objective, immune to hallucination
- [ ] **1.2b** Run review agents in parallel with quality-gate agents of the step → **T6**
- [ ] **1.2c** Find a way to speed review agents up → **T6**
- [ ] **1.3** Quality-gate agents in parallel where possible → **T6** (ceiling note: a gate cannot precede the agent whose output it inspects — parallelizable part is detection, not application)
- [ ] **1.4** Split + parallelize quality-gate agents → **T6** (detection clusters already exist: refactor M/D/T, test-review A/P/Se/S; verify they actually fan out)
- [ ] **1.5a** Parallel red+green cycle per discovered adapter → **T3**
- [ ] **1.5b** Remove human review step between adapter pairs → **T3** (D6)
- [ ] **1.6a** Parallel red-coverage steps (N gaps → N−1 parallel reds + 1 green after) → **T5**
- [ ] **1.6b** Coverage red agents start WITHOUT waiting for the original green's refactor+review batch → **T5** — crosses the atomic-work-unit boundary in `.claude/rules/workflow.md`; commit semantics settled by **D7** (single-committer script, serial commits, prediction-mismatch rule as drift net)
- [ ] **1.7** Verify refactor agents run in parallel; same for test-review → **T6**
- [ ] **1.8** Verify refactor agents run in parallel with review agents → **T6** (designed-in per SKILL.md "Pre-Commit Review Passes"; T1 measures whether reality matches)
- [ ] **1.9a** Frontend: application red-green ∥ client red-green → **T4**
- [ ] **1.9b** Remove human review phase between frontend red-green cycles → **T4** (D6)
- [x] **C1** "Make it deterministic" concern → **D1** (Workflow tool) + **T2**
- [ ] **C2** Estimate every step's potential time cut → **T1** (its required output is a per-step time-budget table, not just raw logs)
- [ ] **C3** Ideal benchmark old vs new → **T1** per **D4** (ceiling-only, fixed commit, story 2 backend scenario 1 + story 1 UI scenario 1)
- [ ] **2** Auto-accept/apply/commit review concerns → **T7** per **D3** (blanket, separate commit, post-factum review)
- [ ] **3a** Downgrade spec-generating `/story` to a `/continue`-internal prompt template → **T8**
- [ ] **3b** New `/story` skill = add story row to backlog in `stories.md` → **T8**
- [ ] **3c** `/story` guardrail: story = pain to cure / opportunity to gain; reject tech stories and part-stories → **T8**

## Surfaced during discussion

- [ ] **N1** Stories growing to ~100 test cases ⇒ multi-week completion — story-size guardrail folded into **T8** (size/splitting check at backlog-add time)
- [ ] **N2** Agents don't help parallelize ACROSS stories/tasks (keeping 3–4 sessions fed) — **not tasked**; README "Open Concerns"; revisit after T1–T2
- [ ] **N3** "Paranoid bureaucratic" gate perception — gate pruning waits for T1 per-gate cost data; README "Open Concerns"
- [x] **N4** Workflow tool billing on subscription → answered, **D2**; no task needed
- [x] **N5** Muscle-memory risk of repurposing `/story` → resolved: nobody invokes it standalone (**D5**)
- [ ] **N6** Review thoroughness drifting down under speed pressure — mitigation via **T7** only; README "Open Concerns"

## Coverage audit

| Source item | Covered by |
|---|---|
| 1.1 | Decision (no task) |
| 1.2 | T1 + T6 |
| 1.3, 1.4, 1.7, 1.8 | T6 |
| 1.5 | T3 |
| 1.6 | T5 |
| 1.9 | T4 |
| Concerns (determinism, estimates, benchmark) | T2/D1, T1, T1/D4 |
| 2 | T7/D3 |
| 3 | T8/D5 |
| New (N1–N6) | T8, D2, D5, open-concerns register |

No initial-prompt item is unmapped.
