# Task 2: Extract Tech Leaks -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Move agent grep patterns to tech bindings
- [x] Move test-review-agent Java grep patterns to `.claude/tech/java-spring/tdd.md` (new "Test Review Grep Patterns" section)
- [x] Replace test-review-agent checklist patterns with generic descriptions + "see tech binding"
- [x] Move refactor-agent Java terms to `.claude/tech/java-spring/coding.md`
- [x] Replace refactor-agent smell detection with generic OOP terms + "see tech binding"
- [x] Move scan-checklist JPA storage checks to `.claude/tech/java-spring/coding.md`
- [x] Replace scan-checklist storage section with generic checks + "see tech binding"

### Step 2: Clean rules — replace named libraries with generic
- [x] Clean `tdd-rules.md` — replace `rest-assured, HttpClient, fetch` with "HTTP testing library"
- [x] Clean `frontend-rules.md` — replace `describe()`, `it()` with generic test structure syntax
- [x] Clean `coding-rules.md` — replace "immutable annotations" with "immutability markers"

### Step 3: Clean skills — replace hardcoded tool names with Conventions references
- [x] Clean `test-coverage/SKILL.md` — replace JaCoCo with "coverage tool (see technology.md Conventions)"
- [x] Clean `test-review/SKILL.md` — replace .java and AssertJ with generic references
- [x] Clean `test-acceptance/SKILL.md` — replace /actuator/health with "health endpoint (see technology.md Conventions)"

### Step 4: Verify nothing lost
- [x] Verify `.claude/tech/java-spring/tdd.md` has all moved test-review grep patterns
- [x] Verify `.claude/tech/java-spring/coding.md` has all moved refactor/scan patterns
- [x] Run grep across `.claude/rules/`, `.claude/agents/`, `.claude/skills/` for remaining Java-specific terms
