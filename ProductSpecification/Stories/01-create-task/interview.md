## Scope

Story 1 covers creating a task on the Kanban board. A task has a **title** (required) and **description** (optional). New tasks always land in the "To Do" column, appended at the bottom (newest last).

### In scope
- Create a task with title + optional description
- Validation: title required, 1-100 chars; description optional, max 5000 chars
- Reject duplicate task titles (unique across the board)
- Task ordering: new tasks appear at the bottom of the To Do column
- Board and columns are pre-existing (seed data — no setup flow)

### Out of scope
- Moving tasks between columns (Story 2)
- Editing or deleting tasks (Story 3)
- Multi-user / authentication
- Board or column management

## Key Architectural Decisions

DECISION: Board + 3 columns (To Do, In Progress, Done) are pre-seeded — they exist when the app starts. No board creation flow.

DECISION: Task titles must be unique across the entire board. Attempting to create a task with a duplicate title returns a validation error.

DECISION: New tasks are appended at the bottom of the To Do column. Ordering is tracked by position (or creation order).

DECISION: Single-user app — no auth, no user model for this story.

## Business Rules & Constraints

- Title: required, 1-100 characters, any text allowed
- Description: optional, max 5000 characters
- Duplicate titles are rejected (case-sensitive match)
- New tasks always go to "To Do" column
- Position within To Do: appended at the bottom (natural queue order)
- Max 100 tasks on the board at any time (from ExpectedLoad)

## Already Implemented (REUSE)

Nothing — greenfield project. Template scaffold only (Spring Boot + React + Gradle multi-module).

## NOT Yet Implemented (Gaps)

- Domain: Task entity, value objects (Title, Description), board/column model
- Usecase: CreateTask usecase, port interfaces (TaskStorage)
- Adapters: REST controller (POST endpoint), H2 storage (JPA entities, Liquibase migration)
- Frontend: Task creation form, board view with To Do column
- Seed data: Liquibase changelog for default board + 3 columns

## Cross-Story Dependencies

- This is the foundational story — Stories 2 and 3 depend on the task/board model established here
- The board + column model created here will be reused by Move Task (Story 2) and Edit/Delete Task (Story 3)
