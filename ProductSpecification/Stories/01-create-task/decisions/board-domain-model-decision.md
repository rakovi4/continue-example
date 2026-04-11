# Decision: Board Domain Model — Seeded via Migration

**Date**: 2026-04-14 **Scenarios**: 1.1, 4.1, 4.2

Board needs three fixed columns. No board management story exists.

| Rejected | Why |
|----------|-----|
| Runtime board creation via API | Unnecessary complexity, no story for it |
| Hardcoded columns in code | Can't customize later, split storage |

**Chosen**: Seed board + columns via Liquibase migration.

## Model

- `Board` — entity, `List<Column> columns` (ordered by position)
- `Column` — entity, `String name`, `int position`, `List<Task> tasks`
- `BoardStorage.getBoard(): Board` — single-board app, no ID parameter
- Seed: board (id=1, "Project Board"), columns: To Do/1, In Progress/2, Done/3
- REST: `GET /api/v1/board` → `BoardResponse` with `ColumnResponse` list
