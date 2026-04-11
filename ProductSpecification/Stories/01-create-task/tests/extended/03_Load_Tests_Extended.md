> These are additional edge case tests. Implement after core tests pass.

## 1. Sustained Load

### 1.1 Sequential task creation performance does not degrade

```gherkin
Given an empty board
When the user creates 100 tasks sequentially
Then the response time of the last task is within 50% of the first task
```

---

## 2. Volume Edge Cases

### 2.1 Duplicate title rejection remains fast at capacity

```gherkin
Given a board with 100 tasks in the To Do column
When the user creates a task with a duplicate title
Then the validation error is returned within 200 milliseconds
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `100 tasks sequentially` | 100 sequential POST /api/v1/tasks with unique titles |
| `within 50% of the first task` | last_response_time < first_response_time * 1.5 |
| `a task with a duplicate title` | POST /api/v1/tasks with title matching an existing task |
