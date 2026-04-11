# Task 6: Deduplicate Tech Templates -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Extract universal test-review-patterns
- [S] refactor — skipped: universal template already has shared catalog (31 anti-patterns, 27 rules, concept table); tech files already contain only language-specific code examples; remaining "duplication" is heading text (~9% of content) which serves navigation

### Step 2: Extract universal coverage-commands
- [x] refactor (extract module mapping table, report format, gap reference to universal template, reduce 7 tech files to tool-specific commands only)

### Step 3: Extract universal acceptance test-class structure
- [x] refactor (extract 3-Tier DSL table + Key Paths to universal template, reduce 7 tech files to framework-specific rules only)

### Step 4: Extract universal red-phase-formats
- [x] refactor (extract universal description to shared template, reduce 7 tech files to marker syntax only)

### Step 5: Extract universal coding.md sections
- [x] refactor (extract shared section headers + explanations to universal coding-principles template, reduce 14 tech files to language-specific constructs only)
