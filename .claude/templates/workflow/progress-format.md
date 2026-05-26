# Progress File Format

## Story

```markdown
# Story N: Story Title — Progress

## Spec
- [x] interview
- [x] story
- [x] mockups
- [x] api-spec
- [x] test-spec

## Backend Scenarios (01_API_Tests.md)

### Scenario 1: Scenario title
- [x] red-acceptance
- [~] design               <- MANDATORY for every scenario needing new implementation
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

#### After adapters-discovery resolves (example):
- [x] adapters-discovery (h2, rest)
- [ ] red-adapter h2
- [ ] green-adapter h2
- [ ] red-adapter rest
- [ ] green-adapter rest
- [ ] green-acceptance

## Integration Scenarios (06_Integration_Tests.md)

### Scenario title
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

## Frontend Scenarios (02_UI_Tests.md)

### Scenario 1: Scenario title
- [ ] red-selenium
- [ ] red-frontend
- [ ] green-frontend
- [ ] red-frontend-api
- [ ] green-frontend-api
- [ ] align-design
- [ ] green-selenium
- [ ] demo

## Security Scenarios (05_Security_Tests.md)

### Scenario title
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

## Load Scenarios (03_Load_Tests.md)

### Scenario title
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

## Infrastructure Scenarios (04_Infrastructure_Tests.md)

### Scenario title
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance
```

## Task (bug)

Bug tasks start with discovery, not pre-planned TDD steps. `steps-discovery` is a gate (analogous to `adapters-discovery` in stories) -- it expands in place into concrete TDD steps once the root cause is known. Prod-copy bugs prepend a `reproduce in prod-copy` step.

```markdown
# Task N: Title — Progress

Type: bug

## Spec
- [x] spec

## Fix: Bug description
- [x] reproduce in prod-copy          <- only for prod-copy bugs
- [~] root cause analysis             <- CURRENT
- [ ] steps discovery
```

After `steps discovery` resolves, `/continue` replaces it with concrete TDD steps for the affected layer(s), e.g.:

```markdown
## Fix: Bug description
- [x] reproduce in prod-copy
- [x] root cause analysis
- [x] steps discovery (frontend logic + component)
- [~] red-frontend                    <- CURRENT
- [ ] green-frontend
- [ ] align-design
- [ ] demo
```

## Task (refactoring)

```markdown
# Task N: Title — Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Step description
- [~] red-adapter h2                  <- CURRENT
- [ ] green-adapter h2

### Step 2: Step description
- [ ] refactor usecase
- [ ] refactor (cleanup)
- [ ] green-acceptance
```
