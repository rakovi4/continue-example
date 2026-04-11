> These are additional edge case tests. Implement after core tests pass.

## 1. Title Edge Cases

### 1.1 Create task with single-character title

```gherkin
When the user creates a task with title "X"
Then the task is created successfully
```

### 1.2 Create task with exactly 100-character title

```gherkin
When the user creates a task with a title of exactly 100 characters
Then the task is created successfully
```

### 1.3 Create task with whitespace-only title

```gherkin
When the user creates a task with title "   "
Then the task is rejected with a validation error
And the error message indicates the title is required
```

---

## 2. Description Edge Cases

### 2.1 Create task with exactly 5000-character description

```gherkin
When the user creates a task with a valid title and a description of exactly 5000 characters
Then the task is created successfully
```

---

## 3. Duplicate Title Edge Cases

### 3.1 Case-sensitive duplicate check

```gherkin
Given a task "Setup CI" exists in To Do
When the user creates a task with title "setup ci"
Then the task is created successfully
```

### 3.2 Duplicate title with different description

```gherkin
Given a task "Setup CI" with description "Version 1" exists in To Do
When the user creates a task with title "Setup CI" and description "Version 2"
Then the task is rejected with a duplicate title error
```

---

## 4. Ordering Edge Cases

### 4.1 Multiple tasks maintain insertion order

```gherkin
Given tasks "Task A", "Task B", "Task C" exist in To Do in that order
When the user requests the board
Then the To Do column contains tasks in order: "Task A", "Task B", "Task C"
```
