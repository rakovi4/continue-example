# Create Task

## Brief Description

User creates a task with a title and optional description. The task appears at the bottom of the "To Do" column on a pre-existing Kanban board.

## Flow

1. User sees the board with three columns: To Do, In Progress, Done
2. User clicks "Add Task" / submits the creation form
3. User enters a title (required) and optional description
4. System validates input and checks for duplicate titles
5. Task is created and appended at the bottom of "To Do"
6. Board refreshes to show the new task

## Acceptance Criteria

- Task created with title and optional description lands in "To Do" column
- New tasks appear at the bottom of the To Do column (append order)
- Duplicate task titles are rejected with an error message
- Empty or blank title is rejected
- Title exceeding 100 characters is rejected
- Description exceeding 5000 characters is rejected
- Board with 3 columns (To Do, In Progress, Done) exists from app start (seed data)

## Validation Rules

| Field       | Rule                                    |
|-------------|-----------------------------------------|
| Title       | Required, 1-100 chars, any text allowed |
| Description | Optional, max 5000 chars                |
| Title       | Unique across board (case-sensitive)    |

## Screen States

- Board view with three columns (default state, empty board)
- Board view with tasks in To Do column
- Task creation form (title + description fields)
- Validation error state (inline field errors)
- Duplicate title error state

## Core Requirements

- Board + 3 columns pre-seeded via Liquibase migration
- Single-user, no authentication
- Task position tracked for ordering within column
- POST endpoint for task creation
- No multi-tenancy, no board management
