# Dependency Tree — Speed-Up Tasks 8–16

Which tasks block which, and how to spread them across 3–4 parallel sessions.
Also the first live specimen of the per-story `dependencies.md` format that
[Task 16](../tasks/16-refactoring-dependency-tree-streams/spec.md) introduces.

## Tree

```
Task 8 (timing + benchmark)
├──> Task 9 (Workflow orchestration)
│    ├──> Task 10 (adapter pipelines)
│    ├──> Task 11 (frontend chains)
│    ├──> Task 12 (coverage-red fan-out)
│    └──> Task 13 (review/gate parallelism)   <── also needs Task 8's data
│
Task 14 (auto-apply)      — no hard deps, but cheaper AFTER 9
Task 15 (/story split)    — independent, do BEFORE 9
Task 16 (streams)         — steps 1–2 now, steps 3–4 AFTER 9
```

## Hard dependencies

| Task | Blocked by | Why |
|---|---|---|
| 9 | 8 | Ceiling baseline must be measured on the old dispatch before 9 rewrites it |
| 10, 11, 12 | 9 | They are Workflow pipeline stages; the scripts must exist first |
| 13 | 8 + 9 | Needs 8's per-gate cost data and 9's scripts |
| 14, 15, 16 | — | Independent (soft ordering below) |

## Soft ordering — file conflicts

The real serializer for parallel sessions is shared files, not data flow:

| Hot file | Touched by |
|---|---|
| `.claude/skills/continue/SKILL.md` | 9 (full rewrite), 13, 14, 15, 16 |
| `.claude/guidelines/workflow-detail.md` | 9, 10, 11, 12, 16 |
| `.claude/rules/workflow.md` | 12, 16 |

Task 9 restructures dispatch entirely, so dispatch-related work written before it
lands is written against a doomed structure:

- **Task 14 after 9** — its apply step becomes a Workflow script stage instead of
  a prose instruction 9 would rewrite.
- **Task 15 before 9** — its SKILL.md touch is one line in the spec-phase
  dispatch; land it while 8 runs and 9 rebases over a trivial diff.
- **Task 16 steps 1–2 anytime** (formats — exactly what 9 needs to coordinate
  with); step 3 (stream-aware `/continue`) is blocked by 9, see its progress file.
- **10, 11, 12 concurrently is mergeable, not conflict-free** — different
  sections of `workflow-detail.md`; whoever lands second rebases.

## Wave plan (3–4 sessions)

| Wave | Sessions | Notes |
|---|---|---|
| 1 (now) | Task 8 hooks stream · Task 8 fixtures stream · Task 15 · Task 16 steps 1–2 | Task 8 splits per its [progress index](../tasks/8-refactoring-agent-timing-benchmark/progress.md); all four disjoint on files |
| 2 (8 done) | Task 9 alone owns SKILL.md + workflow-detail.md; Task 16 finishes formats | 9 is the bottleneck — don't crowd it |
| 3 (9 done) | 3–4 of: 10, 11, 12, 13, 14, 16 steps 3–4 | No hard edges between them; by impact: 10, 13, 11 first, then 12, 14, 16 |

**Critical path: 8 → 9 → 13.** Everything else is slack that fills sessions
around it.
