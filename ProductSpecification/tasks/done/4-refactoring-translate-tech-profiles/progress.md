# Task 4: Translate Tech Profiles -- Progress

Type: refactoring

Blocked by: Task 3 (Composite Tech Profiles)

## Spec
- [x] spec

## Fix

### Step 1: node-ts-express
- [x] create .claude/tech/node-ts-express/coding.md
- [x] create .claude/tech/node-ts-express/tdd.md
- [x] create .claude/tech/node-ts-express/infrastructure.md
- [x] create .claude/tech/node-ts-express/templates/ (usecase, acceptance, rest, db, testing)
- [x] verify — all java-spring concepts have a node-ts-express equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/node-ts-express/)

### Step 2: python-django
- [x] create .claude/tech/python-django/coding.md
- [x] create .claude/tech/python-django/tdd.md
- [x] create .claude/tech/python-django/infrastructure.md
- [x] create .claude/tech/python-django/templates/ (usecase, acceptance, rest, db, testing)
- [x] verify — all java-spring concepts have a python-django equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/python-django/)

### Step 3: csharp-dotnet
- [x] create .claude/tech/csharp-dotnet/coding.md
- [x] create .claude/tech/csharp-dotnet/tdd.md
- [x] create .claude/tech/csharp-dotnet/infrastructure.md
- [x] create .claude/tech/csharp-dotnet/templates/ (usecase, acceptance, rest, db, testing)
- [x] verify — all java-spring concepts have a csharp-dotnet equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/csharp-dotnet/)

### Step 4: php-laravel
- [x] create .claude/tech/php-laravel/coding.md
- [x] create .claude/tech/php-laravel/tdd.md
- [x] create .claude/tech/php-laravel/infrastructure.md
- [x] create .claude/tech/php-laravel/templates/ (usecase, acceptance, rest, db, testing)
- [x] verify — all java-spring concepts have a php-laravel equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/php-laravel/)

### Step 5: cpp-cmake
- [x] create .claude/tech/cpp-cmake/coding.md
- [x] create .claude/tech/cpp-cmake/tdd.md
- [x] create .claude/tech/cpp-cmake/infrastructure.md
- [x] create .claude/tech/cpp-cmake/templates/ (usecase, acceptance, grpc, sqlite, testing)
- [x] verify — all java-spring concepts have a cpp-cmake equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/cpp-cmake/)

### Step 6: go-stdlib
- [x] create .claude/tech/go-stdlib/coding.md
- [x] create .claude/tech/go-stdlib/tdd.md
- [x] create .claude/tech/go-stdlib/infrastructure.md
- [x] create .claude/tech/go-stdlib/templates/ (usecase, acceptance, http, db, testing)
- [x] verify — all java-spring concepts have a go-stdlib equivalent
- [x] audit ecosystem libraries — verify all code examples and recommendations use established ecosystem libs, not hand-written utilities
- [x] /prompt-refactor — step-scoped (.claude/tech/go-stdlib/)

### Step 7: Cross-profile verification
- [x] verify Conventions table mapping — every convention has equivalent in all 7 profiles
- [x] verify template coverage — every template type has equivalent in all profiles
- [x] verify no java-specific concepts leaked into translations (no @Disabled, no Lombok, no JPA) — fixed 1 Lombok reference in cpp-cmake/coding.md
- [x] /prompt-refactor — step-scoped (cpp-cmake/coding.md: removed duplicated Accessor Chains section)

### Section: Fix — post-section refactor
- [x] /prompt-refactor — section-scoped (17 violations fixed across 6 profiles: deduplicated universal rules)

## Final
- [x] /prompt-refactor — task-scoped (java-spring: 1 fix in coding.md)
