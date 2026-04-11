# Task 5: Translate Frontend Profiles -- Progress

Type: refactoring

Blocked by: Task 3 (Composite Tech Profiles)

## Spec
- [x] spec

## Fix

### Step 1: angular-ts
- [x] create .claude/tech/angular-ts/coding.md
- [x] create .claude/tech/angular-ts/tdd.md
- [x] create .claude/tech/angular-ts/infrastructure.md
- [x] create .claude/tech/angular-ts/templates/ (logic-test, api-test, implementation)
- [x] verify — all react-ts concepts have an angular-ts equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/angular-ts/) — 1 fix: Jest→Vitest in coding.md

### Step 2: vue-ts
- [x] create .claude/tech/vue-ts/coding.md
- [x] create .claude/tech/vue-ts/tdd.md
- [x] create .claude/tech/vue-ts/infrastructure.md
- [x] create .claude/tech/vue-ts/templates/ (logic-test, api-test, implementation)
- [x] verify — all react-ts concepts have a vue-ts equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/vue-ts/)

### Step 3: plain-css
- [x] create .claude/tech/plain-css/coding.md
- [x] verify — all tailwind concepts have a plain-css equivalent
- [x] audit ecosystem libraries — all vanilla CSS, no external libs needed. Icons delegated to frontend framework profile.
- [x] /prompt-refactor — step-scoped (.claude/tech/plain-css/)

### Step 4: cypress
- [x] create .claude/tech/cypress/coding.md
- [x] create .claude/tech/cypress/tdd.md
- [x] create .claude/tech/cypress/templates/ (browser-test, statements)
- [x] verify — all selenium concepts have a cypress equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/cypress/)

### Step 5: playwright
- [x] create .claude/tech/playwright/coding.md
- [x] create .claude/tech/playwright/tdd.md
- [x] create .claude/tech/playwright/templates/ (browser-test, statements)
- [x] verify — all selenium concepts have a playwright equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/playwright/)

### Step 6: Cross-profile verification
- [x] verify frontend framework parity — angular-ts and vue-ts cover same concepts as react-ts
- [x] verify browser testing parity — cypress and playwright cover same concepts as selenium
- [x] verify no react-specific concepts leaked into translations — fixed Vitest→Jest in angular-ts (tdd.md, coding.md, templates)
- [x] /prompt-refactor — step-scoped (angular-ts files fixed during verification) — 0 violations

### Section: Fix — post-section refactor
- [x] /prompt-refactor — section-scoped: moved test commands to infrastructure.md, removed duplicate Test File Naming from tdd.md, added Icon Library to angular-ts + react-ts, fixed api-test pre-check path

## Final
- [x] /prompt-refactor — task-scoped (all 20 files) — 0 violations, all clean
