# Task Creation Formats

## Usage Examples

```
/task bug "Modal scroll broken"
/task refactoring "TaskBoard aggregate"
/task qa "Smoke short list"
/task                                        # Interactive
```

## spec.md Format

```markdown
# Task {N}: {Title}

Type: {bug|refactoring|qa}

## Problem

{description}

## Solution

{description}

## Key Files  <- bug and refactoring only (omit for qa)

- {file paths}

## Reproduction  <- bug only

{steps}

## Cases  <- qa only (omit Key Files and Reproduction)

1. {one-line case expressing intent — no Gherkin, no implementation detail}
2. ...
```

## progress.md Formats

### Bug (any layer)

Bug tasks do NOT pre-plan TDD steps at creation time. The progress file starts with a discovery sequence; concrete TDD steps are inserted by `/continue` once `steps-discovery` resolves (see Task Workflow in `.claude/rules/workflow.md`). This applies to every bug regardless of affected layer -- backend, frontend, or both.

```markdown
# Task {N}: {Title} -- Progress

Type: bug

## Spec
- [x] spec

## Fix: {bug description}
- [ ] root cause analysis
- [ ] steps discovery
```

### Bug (prod-copy)

When the bug is observed in prod-copy (or any external environment that needs explicit reproduction before investigation), prepend a `reproduce in prod-copy` step:

```markdown
# Task {N}: {Title} -- Progress

Type: bug

## Spec
- [x] spec

## Fix: {bug description}
- [ ] reproduce in prod-copy
- [ ] root cause analysis
- [ ] steps discovery
```

### Refactoring

```markdown
# Task {N}: {Title} -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: {description}
- [ ] red-adapter h2
- [ ] green-adapter h2

### Step 2: {description}
- [ ] refactor usecase
- [ ] refactor (cleanup)
- [ ] green-acceptance
```

### QA

QA tasks define a reusable manual checklist (smoke / regression) verified against an external environment. There is no TDD cycle and no production code change. `progress.md` mirrors `spec.md`'s Cases section as checkboxes; the tester ticks `[ ]` -> `[x]` during a session. Failed cases stay `[ ]` and a separate bug task is filed for the failure -- never overload the checkbox with a fail marker.

```markdown
# Task {N}: {Title} -- Progress

Type: qa

## Spec
- [x] spec

## Cases
- [ ] {case 1 -- short intent}
- [ ] {case 2 -- short intent}
- [ ] {case 3 -- short intent}
```
