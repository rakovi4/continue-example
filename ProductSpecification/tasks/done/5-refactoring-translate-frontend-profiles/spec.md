# Task 5: Translate Frontend Profiles

Type: refactoring

## Problem

After Task 3 extracts `react-ts`, `tailwind`, and `selenium` as independent concern profiles, the framework only supports one option per concern. Developers using Angular, Vue, plain CSS, Cypress, or Playwright cannot adopt the framework without manually translating frontend/CSS/browser-testing bindings.

## Solution

Create 5 new profiles by translating the reference profiles extracted in Task 3.

### Profiles to create

| # | Profile | Type | Translates from | Framework/Tool |
|---|---------|------|-----------------|---------------|
| 1 | `angular-ts` | Frontend framework | `react-ts` | Angular 17+ / TypeScript |
| 2 | `vue-ts` | Frontend framework | `react-ts` | Vue 3 / Composition API / TypeScript |
| 3 | `plain-css` | CSS | `tailwind` | Vanilla CSS / CSS custom properties |
| 4 | `cypress` | Browser testing | `selenium` | Cypress |
| 5 | `playwright` | Browser testing | `selenium` | Playwright / TypeScript |

### Per-profile deliverables

#### Frontend framework profiles (`angular-ts`, `vue-ts`)

Each needs (directory: `.claude/tech/{profile}/`):

1. **`coding.md`** — Humble object pattern mapped to framework idioms, feature structure, component splitting, naming conventions.
2. **`tdd.md`** — Test runner, HTTP mock library, test skip marker, assertion patterns.
3. **`infrastructure.md`** — Dev server commands, build commands, env variable access.
4. **`templates/`** — Logic test, API client test, component implementation scaffolds.

| Concern | react-ts | angular-ts | vue-ts |
|---------|----------|-----------|--------|
| Component | `.tsx` (JSX + hooks) | `.component.ts` + `.component.html` (templates + decorators) | `.vue` SFC (Composition API) |
| Logic | `.logic.ts` (pure functions) | `.logic.ts` (pure functions) | `.logic.ts` (pure functions) |
| API client | `.api.ts` (fetch) | `.api.ts` (fetch or HttpClient) | `.api.ts` (fetch) |
| Test runner | Vitest | Jest or Karma | Vitest |
| HTTP mock | MSW | MSW or HttpTestingModule | MSW |
| Test skip | `.skip` | `xit`/`xdescribe` or `.skip` | `.skip` |
| Env vars | `import.meta.env.VITE_API_URL` | `environment.ts` | `import.meta.env.VITE_API_URL` |
| Feature dir | `features/{name}/` | `features/{name}/` | `features/{name}/` |

#### CSS profile (`plain-css`)

Simpler — single file:

1. **`coding.md`** — Semantic class naming, CSS custom properties for theming, extraction rules (when to create a class vs inline), responsive patterns. No utility-first approach — traditional stylesheet conventions.

#### Browser testing profiles (`cypress`, `playwright`)

Each needs:

1. **`coding.md`** — 2-tier DSL mapped to tool's API, locator strategy (`data-testid`), navigation rules, mass failure diagnosis.
2. **`tdd.md`** — Test disable marker, assertion patterns, wait/retry patterns, stub/intercept patterns.
3. **`templates/`** — Browser test scaffold, Statements class pattern.

| Concern | selenium | cypress | playwright |
|---------|----------|---------|-----------|
| Language | Java | JavaScript/TypeScript | TypeScript |
| Locators | `By.cssSelector("[data-testid='x']")` | `cy.get("[data-testid='x']")` | `page.getByTestId('x')` |
| Waits | Explicit (`WebDriverWait`) | Auto-retry built-in | Auto-wait built-in |
| Assertions | AssertJ (`assertThat`) | Chai (`should`/`expect`) | Built-in (`expect().toBeVisible()`) |
| HTTP stubs | WireMock (separate process) | `cy.intercept()` (built-in) | `page.route()` (built-in) |
| Test disable | `@Disabled` (JUnit) | `it.skip()` | `test.skip()` |
| Navigation | `driver.get(url)` | `cy.visit(url)` | `page.goto(url)` |

### Translation principles

- Same as Task 4: map concepts, use mainstream conventions, don't force React patterns onto Angular/Vue.
- **Humble object pattern is universal** — all three frontend frameworks separate logic from rendering. Only the rendering syntax differs.
- **Browser testing DSL is universal** — 2-tier pattern (Test Class + Statements) works in all three tools. Only locator/assertion syntax changes.
- **Plain CSS is simpler than Tailwind** — fewer rules, no utility extraction. Focus on semantic naming and custom properties.

## Dependency

**Blocked by Task 3** (Composite Tech Profiles). Task 3 must extract `react-ts`, `tailwind`, and `selenium` as independent profiles before translation begins.

## Key Files

- `.claude/tech/react-ts/` — reference frontend profile (source, created in Task 3)
- `.claude/tech/tailwind/` — reference CSS profile (source, created in Task 3)
- `.claude/tech/selenium/` — reference browser testing profile (source, created in Task 3)
- `.claude/tech/{new-profile}/` — 5 new directories to create
