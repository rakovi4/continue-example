# Task 3: Composite Tech Profiles

Type: refactoring

## Problem

The current tech profile system (`tech-profile: java-spring`) is monolithic — one profile bundles backend (Java/Spring), frontend (React/TypeScript), CSS framework (Tailwind), and browser testing (Selenium). This prevents mix-and-match combinations (e.g., `python-fastapi` + `react-ts` + `tailwind` + `selenium`) and forces every new backend profile to duplicate frontend/CSS/browser content.

## Solution

Split the monolithic profile into **4 independent concern profiles** and update the loading mechanism to resolve composite profiles.

### New profile structure

```
tech-profile:
  backend: java-spring
  frontend: react-ts
  css: tailwind
  browser-testing: selenium
```

### 4 concern profiles to extract

| Profile | Source | Content |
|---------|--------|---------|
| `java-spring` | Keep (trimmed) | `coding.md`, `tdd.md`, `infrastructure.md` — backend-only. Remove frontend/CSS/Selenium content |
| `react-ts` | Extract from `java-spring/frontend.md` | `coding.md` (humble object, feature structure, naming), `tdd.md` (Vitest, MSW, `.skip` marker), `infrastructure.md` (Vite commands, `VITE_API_URL`), `templates/frontend/` (logic-test, api-test, implementation) |
| `tailwind` | Extract from `java-spring/frontend.md` | `coding.md` (utility extraction rules, `@apply`, theme.css), icon library reference (lucide-react) |
| `selenium` | Extract from `java-spring/frontend.md` + `java-spring/tdd.md` | `coding.md` (2-tier DSL, `data-testid`, locator rules), `tdd.md` (Selenium Statements assertions, WireMock stubs), `templates/` (selenium-test.md) |

### Loading mechanism changes

1. `ProductSpecification/technology.md` — change from single `tech-profile:` to composite `tech-profile:` with concern keys
2. `.claude/rules/technology-loading.md` — update resolution protocol for composite profiles
3. All 20+ agents/skills that read `technology.md` — update to resolve the correct concern profile per layer
4. `.claude/technology-references.md` — update documentation

### Directory structure after

```
.claude/tech/
  java-spring/          # Backend only (trimmed)
    coding.md
    tdd.md
    infrastructure.md
    templates/
      usecase/
      acceptance/
      rest/
      h2/
      email/
      scheduling/
      security/
      testing/
      infrastructure/
  react-ts/             # Frontend framework
    coding.md
    tdd.md
    infrastructure.md
    templates/
      logic-test.md
      api-test.md
      implementation.md
  tailwind/             # CSS framework
    coding.md
  selenium/             # Browser testing
    coding.md
    tdd.md
    templates/
      selenium-test.md
      align-design-checklist.md
      design-review-patterns.md
```

## Key Files

- `ProductSpecification/technology.md` — profile declaration
- `.claude/rules/technology-loading.md` — loading protocol
- `.claude/technology-references.md` — documentation
- `.claude/tech/java-spring/frontend.md` — source to split
- `.claude/tech/java-spring/tdd.md` — Selenium content to extract
- `.claude/tech/java-spring/coding.md` — stays (backend only, already clean)
- `.claude/tech/java-spring/infrastructure.md` — remove frontend commands
- `.claude/tech/java-spring/templates/frontend/` — 6 files to redistribute
- 20+ agent/skill files that reference `technology.md` — update profile resolution
