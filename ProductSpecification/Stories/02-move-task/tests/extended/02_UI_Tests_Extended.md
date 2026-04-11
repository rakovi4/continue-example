> These are additional edge case tests. Implement after core tests pass.

## 1. Edge Cases

### 1.1 Move task on board with single task

```gherkin
Given only one task exists on the board in To Do
When the user moves it to Done
Then To Do is empty
And Done contains the task
```

### 1.2 Rapid consecutive moves

```gherkin
Given a task exists in To Do
When the user moves the task to In Progress
And immediately moves it to Done
Then the task ends up in Done
And all columns show correct state
```
