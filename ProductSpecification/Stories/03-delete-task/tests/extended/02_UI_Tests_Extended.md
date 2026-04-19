> These are additional edge case tests. Implement after core tests pass.

## 1. Delete from Different Columns

### 1.1 Delete task from In Progress column via UI

```gherkin
Given a task "In flight" exists in In Progress
And the user opens the board page
When the user clicks the delete button on task "In flight"
And the user confirms the deletion
Then the task "In flight" is no longer visible on the board
```

### 1.2 Delete task from Done column via UI

```gherkin
Given a task "Completed" exists in Done
And the user opens the board page
When the user clicks the delete button on task "Completed"
And the user confirms the deletion
Then the task "Completed" is no longer visible on the board
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task "X" exists in In Progress` | Create task then move to In Progress (backend setup) |
| `a task "X" exists in Done` | Create task then move to Done (backend setup) |
