> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with empty board state, then input validation, then task creation happy path, then board state with tasks, then duplicate rejection.

## 1. Read Empty Board State

### 1.1 Get empty board

```gherkin
Given a board with three columns
When the user requests the board
Then the response contains the board with columns To Do, In Progress, Done
And each column has no tasks
```

---

## 2. Create Task — Validation

### 2.1 Reject empty title

```gherkin
When the user creates a task with an empty title
Then the task is rejected with a validation error
And the error message indicates the title is required
```

### 2.2 Reject title exceeding 100 characters

```gherkin
When the user creates a task with a title of 101 characters
Then the task is rejected with a validation error
And the error message indicates the title is too long
```

### 2.3 Reject description exceeding 5000 characters

```gherkin
When the user creates a task with a valid title and a description of 5001 characters
Then the task is rejected with a validation error
And the error message indicates the description is too long
```

---

## 3. Create Task — Happy Path

### 3.1 Create task with title only

```gherkin
When the user creates a task with title "Set up CI/CD"
Then the task is created successfully
And the response contains the task with title "Set up CI/CD" and no description
And the task has a position and creation timestamp
```

### 3.2 Create task with title and description

```gherkin
When the user creates a task with title "Set up CI/CD" and description "Configure GitHub Actions"
Then the task is created successfully
And the response contains the task with title "Set up CI/CD" and description "Configure GitHub Actions"
```

---

## 4. Board State After Task Creation

### 4.1 Get board with tasks in To Do

```gherkin
Given a task "Set up CI/CD" exists in To Do
When the user requests the board
Then the To Do column contains task "Set up CI/CD"
And In Progress and Done columns have no tasks
```

### 4.2 New task appears at bottom of To Do column

```gherkin
Given a task "First task" exists in To Do
When the user creates a task with title "Second task"
Then the task is created successfully
When the user requests the board
Then the To Do column contains "First task" before "Second task"
```

---

## 5. Create Task — Duplicate Rejection

### 5.1 Reject duplicate task title

```gherkin
Given a task "Set up CI/CD" exists in To Do
When the user creates a task with title "Set up CI/CD"
Then the task is rejected with a duplicate title error
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a board with three columns` | GET /api/v1/board returns pre-seeded board |
| `a task "X" exists in To Do` | POST /api/v1/tasks with title "X" (setup) |
| `the user requests the board` | GET /api/v1/board |
| `the user creates a task with title "X"` | POST /api/v1/tasks `{"title": "X"}` |
| `the user creates a task with title "X" and description "Y"` | POST /api/v1/tasks `{"title": "X", "description": "Y"}` |
| `the user creates a task with an empty title` | POST /api/v1/tasks `{"title": ""}` |
| `the task is created successfully` | HTTP 201, response body matches TaskResponse |
| `the task is rejected with a validation error` | HTTP 400, error: "VALIDATION_ERROR" |
| `the task is rejected with a duplicate title error` | HTTP 400, error about duplicate title |
