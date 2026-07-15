# Task 12: Coverage-Red Fan-Out + Cross-Unit Overlap

Type: refactoring

Framework speed-up initiative **T5** — see
[framework-speedup/README.md](../../framework-speedup/README.md) (D7) and
[checklist items 1.6a, 1.6b](../../framework-speedup/checklist.md).

## Problem

Two serializations around the coverage step:

1. **Within the coverage batch.** When `coverage-agent` finds N gaps, the
   resulting red steps run one at a time. N independent gaps should be N
   parallel red agents followed by one green.
2. **Across the work-unit boundary.** Coverage red agents currently wait for
   the original green's full refactor + review batch to finish. That wait is
   unnecessary: the reds write new tests; the refactor batch reshapes existing
   code. Removing the wait crosses the atomic-work-unit boundary defined in
   `.claude/rules/workflow.md`, which is exactly why it needs an explicit rule
   change plus the D7 commit semantics — otherwise concurrent units racing to
   commit would pool changes into unattributable mega-commits.

## Solution

In the Workflow orchestration (Task 9):

- N gaps → N parallel red agents, one green agent after all reds land.
- Coverage red agents start while the original green's refactor + review batch
  is still running — no barrier between the two units.
- Commit race resolved by D7: agents don't commit; the Workflow script is the
  single committer, serializing commits at defined points, staging by pathspec
  from each agent's returned file list. Refactor-vs-coverage attribution is
  preserved because each logical unit still lands as its own commit.
- Residual rename-vs-new-test semantic drift is caught by the red
  prediction-mismatch rule.
- Update the atomic-work-unit wording in `.claude/rules/workflow.md` and the
  coverage sequence in `workflow-detail.md` / `tdd-rules.md` to reflect the
  sanctioned overlap.

Expected impact (hypothesis until Task 8 numbers): medium, ~5–10%.

## Key Files

- `.claude/rules/workflow.md` (atomic work unit boundary)
- `.claude/guidelines/workflow-detail.md` (coverage sequence)
- `.claude/guidelines/tdd-rules.md`
- `.claude/agents/coverage-agent.md`
- `.claude/workflows/` (script from Task 9)

## Dependencies

After Task 9 (Workflow orchestration + D7 commit discipline).
