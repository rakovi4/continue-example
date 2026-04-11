# Decision: Domain Validation via Value Objects

**Date**: 2026-04-14 **Scenarios**: 2.1, 2.2, 2.3

Need consistent validation pattern for task creation inputs → HTTP 400.

| Rejected | Why |
|----------|-----|
| Controller-level validation | Scatters domain rules across adapters |
| Usecase-level validation | Duplicates logic; domain objects can exist invalid |

**Chosen**: Value objects validate in constructor, throw `ValidationException`.

## Model

- `Title` — rejects blank/null (`"Title is required"`), rejects >100 chars (`"Title must not exceed 100 characters"`)
- `Description` — rejects >5000 chars
- `ValidationException` extends base domain unchecked exception
- REST handler: `ValidationException` → 400 `{"error": "VALIDATION_ERROR", "message": "...", "timestamp": "..."}`

## Edge Cases

| Case | Behavior |
|------|----------|
| Empty/blank/null title | `ValidationException("Title is required")` |
| Title 101 chars | `ValidationException("Title must not exceed 100 characters")` |
| Title exactly 100 chars | Accepted |
