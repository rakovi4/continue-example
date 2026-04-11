> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with single request baseline, then concurrent load, then volume.

## 1. Response Time

### 1.1 Single task creation completes within 200ms

```gherkin
Given an empty board
When the user creates a task
Then the response is returned within 200 milliseconds
```

### 1.2 Board retrieval with 100 tasks completes within 200ms

```gherkin
Given a board with 100 tasks in the To Do column
When the user requests the board
Then the response is returned within 200 milliseconds
```

---

## 2. Concurrent Load

### 2.1 Concurrent task creation under load

```gherkin
Given an empty board
When 50 users create tasks simultaneously
Then all requests complete within 500 milliseconds
And all tasks are created successfully with correct positions
```

---

## 3. Volume

### 3.1 Task creation with maximum-length fields

```gherkin
Given an empty board
When the user creates a task with a 100-character title and 5000-character description
Then the task is created successfully
And the response is returned within 200 milliseconds
```

### 3.2 Board at capacity remains responsive

```gherkin
Given a board with 100 tasks in the To Do column
When the user requests the board
Then all 100 tasks are returned
And the response is returned within 200 milliseconds
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `an empty board` | Board pre-seeded via Liquibase with 3 columns, no tasks |
| `a board with 100 tasks in the To Do column` | Seed 100 tasks via POST /api/v1/tasks |
| `the user creates a task` | POST /api/v1/tasks with valid title |
| `the user requests the board` | GET /api/v1/board |
| `50 users create tasks simultaneously` | 50 concurrent POST /api/v1/tasks with unique titles |
| `the response is returned within N milliseconds` | Measure response time, assert < N ms |
| `all requests complete within 500 milliseconds` | 95th percentile response time < 500ms |
| `a 100-character title and 5000-character description` | Title at max 100 chars, description at max 5000 chars |
