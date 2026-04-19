> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with delete button display, then confirmation dialog, then successful deletion, then cancel flow.

## 1. Delete Button Display

### 1.1 Task card shows delete button

```gherkin
Given a task "Clean up logs" exists on the board
When the user opens the board page
Then each task card displays a delete button
```

---

## 2. Confirmation Dialog

### 2.1 Confirmation dialog appears on delete click

```gherkin
Given a task "Clean up logs" exists on the board
And the user opens the board page
When the user clicks the delete button on task "Clean up logs"
Then a confirmation dialog is displayed asking to confirm deletion
And the dialog has confirm and cancel actions
```

### 2.2 Cancel deletion keeps the task

```gherkin
Given a task "Clean up logs" exists on the board
And the user opens the board page
When the user clicks the delete button on task "Clean up logs"
And the user cancels the confirmation dialog
Then the task "Clean up logs" is still visible in its column
```

---

## 3. Successful Deletion

### 3.1 Confirm deletion removes the task from the board

```gherkin
Given a task "Clean up logs" exists on the board
And the user opens the board page
When the user clicks the delete button on task "Clean up logs"
And the user confirms the deletion
Then the task "Clean up logs" is no longer visible on the board
```

### 3.2 Remaining tasks reorder after deletion

```gherkin
Given tasks "First", "Second", "Third" exist in To Do
And the user opens the board page
When the user deletes task "Second" via the delete button and confirms
Then the To Do column shows "First" followed by "Third" with no gaps
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task "X" exists on the board` | POST /api/v1/tasks `{"title": "X"}` (backend setup) |
| `tasks "X", "Y", "Z" exist in To Do` | POST /api/v1/tasks for each (backend setup) |
| `the user opens the board page` | Navigate to app root URL |
| `each task card displays a delete button` | Button with data-testid for delete on each task card |
| `the user clicks the delete button on task "X"` | Click delete button on task card with title "X" |
| `a confirmation dialog is displayed asking to confirm deletion` | Modal/dialog element visible with confirmation text |
| `the dialog has confirm and cancel actions` | Confirm and cancel buttons visible in dialog |
| `the user cancels the confirmation dialog` | Click cancel button in dialog |
| `the user confirms the deletion` | Click confirm button in dialog |
| `the task "X" is still visible in its column` | Task card with title "X" present in column |
| `the task "X" is no longer visible on the board` | No task card with title "X" on the page |
| `the user deletes task "X" via the delete button and confirms` | Click delete on "X", then click confirm |
| `the To Do column shows "X" followed by "Y" with no gaps` | Task cards in To Do in order: "X", "Y" |
