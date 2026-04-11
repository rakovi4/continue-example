# Task 7: Prompt Framework Bottlenecks

Type: refactoring

## Problem

After the tech-agnostic refactoring (Tasks 1-6), the prompt framework has structural bottlenecks that add unnecessary overhead to every `/continue` work unit:

1. **test-review-patterns.md monolith** — The tech-specific file (e.g., java-spring: 540 lines) contains ALL adapter type examples. When reviewing a usecase test, 80% of loaded content is irrelevant (rest patterns, h2 patterns, etc.). Combined with the universal file (135 lines), it's 675 lines — heavier than Repryz1's single 646-line file.

2. **Repeated tech profile resolution** — Every tech-aware sub-skill independently reads `ProductSpecification/technology.md` to resolve the profile name, then reads binding files. In a work unit with 3 sub-skill chains, that's 3+ redundant reads of the same file.

3. **smell-routing-table.md indirection** — An 86-line file that exists solely to map smells → template files, loaded as a separate file read during every `/refactor` invocation. Could be a section in scan-checklist.md.

## Solution

### Step 1: Split test-review-patterns by layer

Split each tech profile's `test-review-patterns.md` into per-layer files:
- `test-review-usecase.md` — usecase assertion patterns
- `test-review-rest.md` — REST controller test patterns
- `test-review-h2.md` — persistence test patterns
- `test-review-acceptance.md` — acceptance test patterns
- `test-review-other.md` — email, scheduling, security (small enough to combine)

Update `test-review-agent.md` to load only the file matching the current test's layer. The agent already knows which layer it's reviewing (from the skill that invoked it).

Applies to all 7 backend tech profiles.

### Step 2: Pass tech profile as parameter

Modify `/continue` skill to pass resolved tech profile names to sub-skills. Sub-skills skip re-reading `technology.md` when profile is provided. Falls back to reading `technology.md` when invoked standalone (not via `/continue`).

### Step 3: Merge smell-routing-table into scan-checklist

Move the smell → fix → template routing table from `smell-routing-table.md` into a new section at the end of `scan-checklist.md`. Delete the standalone file. Update `refactor-agent.md` to remove the separate load instruction.

## Key Files

- `.claude/tech/*/templates/testing/test-review-patterns.md` (7 files — one per backend profile)
- `.claude/templates/testing/test-review-patterns.md` (universal)
- `.claude/agents/test-review-agent.md`
- `.claude/agents/refactor-agent.md`
- `.claude/templates/refactoring/scan-checklist.md`
- `.claude/templates/refactoring/smell-routing-table.md`
- `.claude/skills/continue/SKILL.md`
- All tech-aware skill files (red-usecase, green-usecase, red-adapter, green-adapter, etc.)
