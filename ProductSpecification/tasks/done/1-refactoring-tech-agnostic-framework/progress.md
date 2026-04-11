# Task 1: Tech-Agnostic Framework -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Create technology profile format and tech/ directory structure
- [x] Create `ProductSpecification/technology.md` with java-spring profile declaration
- [x] Create `.claude/tech/java-spring/` directory structure
- [x] Add profile loading convention to a rules file (how agents/skills resolve tech bindings)

### Step 2: Extract tech-specific lines from rules into tech bindings
- [x] Split `rules/coding-rules.md` → universal principles + `tech/java-spring/coding.md`
- [x] Split `rules/tdd-rules.md` → universal TDD rules + `tech/java-spring/tdd.md`
- [x] Split `rules/frontend-rules.md` → universal frontend rules + `tech/java-spring/frontend.md`
- [x] Split `rules/infrastructure.md` → universal infra rules + `tech/java-spring/infrastructure.md`

### Step 3: Move templates under tech profile
- [x] Move `.claude/templates/` contents to `.claude/tech/java-spring/templates/`
- [x] Update all template path references in agents and skills

### Step 4: Update agents to load tech bindings from profile
- [x] Update `agents/red-agent.md` — replace hardcoded Java references with profile lookup
- [x] Update `agents/green-agent.md` — replace hardcoded Java references with profile lookup
- [x] Update `agents/refactor-agent.md` — replace hardcoded Java references with profile lookup
- [x] Update `agents/coverage-agent.md` — replace JaCoCo references with profile lookup
- [x] Update `agents/test-runner.md` — replace Gradle/Vitest references with profile lookup
- [x] Update remaining agents (`test-review-agent.md`, `design-review-agent.md`)

### Step 5: Update skills to resolve template paths via profile
- [x] Update red/green skills (red-usecase, green-usecase, red-adapter, green-adapter, etc.)
- [x] Update frontend skills (red-frontend, green-frontend, red-selenium, green-selenium, etc.)
- [x] Update utility skills (test-coverage, test-all, test-adapter, test-usecase, test-frontend, etc.)
- [x] Update continue skill dispatch (if any tech-specific references)
  - continue skill already tech-agnostic (dispatches via sub-skills only)

### Step 6: Update CLAUDE.md to be tech-agnostic
- [x] Replace hardcoded Spring Boot/React/Gradle references with profile-driven description
- [x] Keep architecture diagram universal (application → adapters → usecase → domain)

### Step 7: Verify full /continue flow still works
- [x] Update `.claude/technology-references.md` to reflect new structure
