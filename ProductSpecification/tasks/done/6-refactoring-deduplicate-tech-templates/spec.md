# Task 6: Deduplicate Tech Templates

Type: refactoring

## Problem

Tech templates under `.claude/tech/{profile}/templates/` contain 60-85% duplicated content across 7 backend profiles. Universal concepts (anti-pattern catalogs, report format tables, DSL structure descriptions, module mapping tables) are copy-pasted with only language-specific syntax differing. Total waste: ~3,500 lines.

Worst offenders by duplication:
- `test-review-patterns.md` — 85% shared (~2,100 lines)
- `coverage-commands.md` — 70% shared (~580 lines)
- `acceptance/test-class.md` — 65% shared (~140 lines)
- `red-phase-formats.md` — 90% shared (~130 lines)
- `coding.md` — 60% shared (~420 lines)

## Solution

For each duplicated file type: extract universal content to `.claude/templates/` (or existing universal files), keep only language-specific examples/syntax in tech profiles. Tech profiles reference the universal file and add only their delta.

Pattern per file:
1. Identify shared sections across all 7 backend profiles
2. Extract shared content to universal template
3. Reduce each tech profile file to: reference to universal + language-specific code examples only
4. Verify all agents/skills that load these files still resolve correctly

## Key Files

### Targets (7 copies each)
- `.claude/tech/{profile}/templates/testing/test-review-patterns.md`
- `.claude/tech/{profile}/templates/testing/coverage-commands.md`
- `.claude/tech/{profile}/templates/acceptance/test-class.md`
- `.claude/tech/{profile}/templates/testing/red-phase-formats.md`
- `.claude/tech/{profile}/coding.md`

### Universal destinations
- `.claude/templates/testing/` (new universal files)
- `.claude/templates/` (existing universal templates)

### Referencing agents/skills
- `.claude/agents/test-review-agent.md`
- `.claude/agents/coverage-agent.md`
- `.claude/agents/red-agent.md`
- `.claude/skills/` (various skills that load tech templates)
