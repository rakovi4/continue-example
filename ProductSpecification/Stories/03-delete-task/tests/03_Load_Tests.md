> **Implementation Order**: Tests are numbered for sequential TDD implementation.

## 1. Single Request Performance

### 1.1 Delete task responds within 200ms

```gherkin
Given a task exists on the board
When the user deletes the task
Then the response is received within 200 milliseconds
```

---

## 2. Volume

### 2.1 Delete task from a full board

```gherkin
Given a board with 100 tasks distributed across columns
When the user deletes a task from the largest column
Then the response is received within 200 milliseconds
And remaining tasks in the column have contiguous positions
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task exists on the board` | POST /api/v1/tasks (setup) |
| `a board with 100 tasks distributed across columns` | Create 100 tasks, move to distribute (setup) |
| `the user deletes the task` | DELETE /api/v1/tasks/{id} |
| `the response is received within N milliseconds` | Response time < N ms |
| `remaining tasks in the column have contiguous positions` | Positions are sequential 1..N |
