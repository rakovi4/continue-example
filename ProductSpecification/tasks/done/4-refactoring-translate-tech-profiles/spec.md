# Task 4: Translate Tech Profiles

Type: refactoring

## Problem

The framework only has one tech profile (`java-spring`). Developers using other languages/frameworks cannot adopt the Clean Architecture + TDD workflow without manually translating all tech bindings and templates. This limits the framework to ~8% of the developer market (Java).

## Solution

Create 6 new tech profiles by translating the `java-spring` reference profile (after Task 3 splits it into backend-only). Each profile provides: coding idioms, TDD idioms, infrastructure commands, and code templates — all mapped to the target stack's conventions.

### Profiles to create

| # | Profile | Language | Framework | Build | Test | DI approach |
|---|---------|----------|-----------|-------|------|-------------|
| 1 | `node-ts-express` | TypeScript | Express | npm/pnpm | Jest/Vitest | Manual constructor injection |
| 2 | `python-django` | Python | Django | pip/poetry | pytest | Manual constructor injection |
| 3 | `csharp-dotnet` | C# | ASP.NET Core | dotnet CLI | xUnit/NUnit | Framework (built-in DI) |
| 4 | `php-laravel` | PHP | Laravel | Composer | PHPUnit/Pest | Framework (Service Container) |
| 5 | `cpp-cmake` | C++ | None (stdlib) | CMake | Google Test + Google Mock | Manual constructor injection |
| 6 | `go-stdlib` | Go | net/http + chi | go build | go test + testify | Manual constructor injection |

### Per-profile deliverables

Each profile needs (directory: `.claude/tech/{profile}/`):

1. **`coding.md`** — Clean Architecture enforcement in target language, DDD idioms, code style, naming, immutability patterns, null handling, error handling. Translated from `java-spring/coding.md`.

2. **`tdd.md`** — Test disable marker, stub pattern, assertion library patterns, async wait patterns, coverage tool, test filter flag, 3-tier test architecture specifics. Translated from `java-spring/tdd.md`.

3. **`infrastructure.md`** — Health check endpoint, process safety, config fallback syntax, acceptance test commands. Translated from `java-spring/infrastructure.md`.

4. **`templates/`** — Code scaffolds for each adapter type. Translated from `java-spring/templates/`. Template subdirectories vary by profile (not all profiles need all adapter types):
   - `usecase/` — all profiles
   - `acceptance/` — all profiles
   - `rest/` or `grpc/` or `http/` — depends on profile
   - Storage adapter (`h2/`, `sqlite/`, `db/`) — depends on profile
   - `testing/` (coverage commands, red-phase formats, test-review patterns) — all profiles

### Translation principles

- **Map concepts, don't copy syntax.** `@Disabled` → `pytest.mark.skip` → `t.Skip()` → `[Fact(Skip="...")]`. Same TDD concept, different idiom.
- **Use the language's mainstream conventions.** Don't force Java patterns onto Go. Go doesn't use exceptions — use error returns. Python doesn't use interfaces — use protocols/ABC.
- **DI: only if the ecosystem expects it.** Spring DI, ASP.NET Core DI, Laravel Container — yes. Python, Go, C++, Node — manual constructor injection.
- **Adapt Clean Architecture to the framework's grain.** Django's ORM is opinionated — document when to use Django models directly vs when to add a domain layer. Laravel's Eloquent — same approach.
- **Keep the same file structure** as `java-spring` so the loading mechanism works identically.

### Future profiles (out of scope)

- `python-fastapi` — second Python profile, deferred (straightforward translation from django)
- `ruby-rails` — deferred (declining market share)
- `kotlin-spring` — covered by `java-spring` with minor syntax differences
- `swift-vapor` — iOS/macOS niche, deferred

## Dependency

**Blocked by Task 3** (Composite Tech Profiles). Task 3 must split `java-spring` into backend-only before translation begins, otherwise new profiles would need to include frontend/CSS/Selenium content that should be shared.

## Key Files

- `.claude/tech/java-spring/` — reference profile (source for translation)
- `.claude/tech/{new-profile}/` — 6 new directories to create
- `ProductSpecification/technology.md` — Conventions table needs per-profile variants
- `.claude/rules/technology-loading.md` — no changes needed (profiles follow same structure)
