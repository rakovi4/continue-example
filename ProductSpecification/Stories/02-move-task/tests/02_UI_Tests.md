> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with board display with tasks, then move interaction, then reorder interaction, then post-move board state.

## 1. Board Display With Tasks

### 1.1 Display board with tasks in multiple columns

```gherkin
Given tasks exist across multiple columns
When the user opens the board page
Then each column displays its tasks in correct order
And each task card shows its title
```

---

## 2. Move Task Interaction

### 2.1 Move task before existing tasks in another column

```gherkin
Given tasks exist in In Progress
And a task exists in To Do
When the user moves the task from To Do to the beginning of In Progress
Then the task appears first in In Progress
And the task is removed from To Do
```

### 2.2 Move task between existing tasks in another column

```gherkin
Given tasks exist in In Progress
And a task exists in To Do
When the user moves the task from To Do between tasks in In Progress
Then the task appears at the chosen position in In Progress
And surrounding tasks shift to accommodate it
```

### 2.3 Move task after existing tasks in another column

```gherkin
Given tasks exist in In Progress
And a task exists in To Do
When the user moves the task from To Do to the end of In Progress
Then the task appears last in In Progress
And the task is removed from To Do
```

### 2.4 Display error when move fails

```gherkin
Given a task exists in To Do
When the user moves the task and the server returns an error
Then an error message is displayed
And the task remains in its original column
```

---

## 3. Reorder Task Interaction

### 3.1 Reorder task within same column

```gherkin
Given multiple tasks exist in To Do
When the user reorders a task to a different position in To Do
Then the tasks appear in the new order
```

---

## 4. Post-Move Board State

### 4.1 Moved task highlighted briefly

```gherkin
Given a task exists in To Do
When the user moves the task to In Progress
Then the moved task is visually highlighted in its new position
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `tasks exist across multiple columns` | API setup: create tasks, move some to other columns |
| `the user opens the board page` | Navigate to app root URL |
| `each column displays its tasks` | Task cards visible in respective column containers |
| `the user moves the task from To Do to the beginning of In Progress` | Drag-and-drop to position 0 in target column |
| `the user moves the task from To Do between tasks in In Progress` | Drag-and-drop to middle position in target column |
| `the user moves the task from To Do to the end of In Progress` | Drag-and-drop to last position in target column |
| `the task appears first/last/at the chosen position in In Progress` | Task card at expected index in column container |
| `the user reorders a task` | Drag within same column or reorder action |
| `an error message is displayed` | Error toast or banner visible |
| `the moved task is visually highlighted` | Task card has highlight styling (border/shadow) |
