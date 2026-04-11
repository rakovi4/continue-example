# Task 1: Tech-Agnostic Framework

Type: refactoring

## Problem

The `.claude/` framework (rules, agents, skills, templates) is deeply coupled to a specific technology stack (Java/Spring Boot/Gradle/JPA + React/Vite/Vitest). Technology references are hardcoded across ~580 locations in 30+ technologies (audited in `.claude/technology-references.md`). This prevents reusing the framework with a different tech stack (e.g., Python/FastAPI, Go/Chi, Kotlin/Spring).

The coupling exists at three levels:
- **Templates** (deepest): full Java class stubs, `com.example.*` paths, JaCoCo commands, JPA annotations
- **Rules** (medium): `Optional<T>`, `instanceof` ban, `ResponseEntity.ok()`, `@DataJpaTest` idioms
- **Agents/Skills** (shallow): `@Disabled` vs `.skip`, `./gradlew`, `npx vitest` command references

## Solution

Split every tech-coupled file into two layers (Option C from discussion):
1. **Universal layer** — principles and architecture rules that apply to any stack (stay in current locations)
2. **Tech binding** — implementation idioms, code snippets, commands, paths (move to `tech/{profile}/`)

A `ProductSpecification/technology.md` file declares the active tech profile. Skills/agents read this file to resolve which tech binding and templates to load.

Directory structure:
```
ProductSpecification/technology.md          # Declares tech-profile: java-spring
.claude/tech/java-spring/
  coding.md                                 # Java/Spring idioms extracted from coding-rules.md
  tdd.md                                    # JUnit/AssertJ/Mockito idioms from tdd-rules.md
  frontend.md                               # React/Vite/Vitest idioms from frontend-rules.md
  infrastructure.md                         # Gradle/Docker/Spring specifics from infrastructure.md
  templates/                                # Current templates moved here
    usecase/
    acceptance/
    rest/
    h2/
    email/
    ...
```

## Key Files

- `.claude/technology-references.md` — audit of all hardcoded references (the extraction map)
- `.claude/rules/coding-rules.md` — heaviest tech coupling in rules
- `.claude/rules/tdd-rules.md` — JUnit/AssertJ/JaCoCo references
- `.claude/rules/frontend-rules.md` — React/Vite/Vitest references
- `.claude/rules/infrastructure.md` — Gradle/Docker/Spring references
- `.claude/templates/` — all template directories
- `.claude/agents/*.md` — agent files with tech references
- `.claude/skills/*/SKILL.md` — skill files with tech references
- `CLAUDE.md` — top-level project description
