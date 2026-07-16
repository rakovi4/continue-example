# Decision: Board Aggregate Owns All Mutations

**Date**: 2026-04-15 **Scenarios**: 3.1, 3.2, 4.1, 4.2, 5.1

`CreateTaskUseCase` bypassed Board aggregate — position logic in usecase, no column association.

| Rejected | Why |
|----------|-----|
| Task as independent entity | Position logic in usecase, race conditions, hollow aggregate |
| Column as aggregate boundary | Loses board-level invariants (duplicate title check) |

**Chosen**: Board as true aggregate root. Load → mutate → save.

## Model

- `Board.addTask(Title, Description): Task` — finds "To Do" column, position = column.tasks.size()
- `BoardStorage.save(Board)` — persists full aggregate graph
- `TaskStorage` — removed
