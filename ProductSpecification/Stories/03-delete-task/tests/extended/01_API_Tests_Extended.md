> These are additional edge case tests. Implement after core tests pass.

## 1. Delete Edge Cases

### 1.1 Delete the only task in a column

```gherkin
Given a single task "Lonely task" exists in To Do
When the user deletes the task "Lonely task"
Then the task is deleted successfully
When the user requests the board
Then the To Do column has no tasks
```

### 1.2 Delete task from In Progress column

```gherkin
Given a task "In flight" exists in In Progress
When the user deletes the task "In flight"
Then the task is deleted successfully
When the user requests the board
Then the In Progress column does not contain "In flight"
```

### 1.3 Delete task from Done column

```gherkin
Given a task "Completed work" exists in Done
When the user deletes the task "Completed work"
Then the task is deleted successfully
When the user requests the board
Then the Done column does not contain "Completed work"
```

### 1.4 Delete same task twice returns not found on second attempt

```gherkin
Given a task "One-time task" exists in To Do
When the user deletes the task "One-time task"
Then the task is deleted successfully
When the user deletes the same task again
Then the request is rejected with a not found error
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task "X" exists in In Progress` | Create task then move to In Progress (setup) |
| `a task "X" exists in Done` | Create task then move to Done (setup) |
| `the user deletes the same task again` | DELETE /api/v1/tasks/{same-id} |
