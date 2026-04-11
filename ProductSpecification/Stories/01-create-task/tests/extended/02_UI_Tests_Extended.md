> These are additional edge case tests. Implement after core tests pass.

## 1. Form Interaction

### 1.1 Cancel task creation

```gherkin
Given the user opens the task creation form
When the user cancels the form
Then the form is closed
And no task is created
```

### 1.2 Form fields are cleared after successful creation

```gherkin
Given the user creates a task with title "First task"
When the user opens the task creation form again
Then the title and description fields are empty
```

---

## 2. Display Edge Cases

### 2.1 Long title displays correctly

```gherkin
Given a task with a 100-character title exists in To Do
When the user views the board
Then the task title is displayed without layout breakage
```
