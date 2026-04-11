> These are additional edge case tests. Implement after core tests pass.

## 1. Cross-Column Edge Cases

### 1.1 Move task back from Done to To Do

```gherkin
Given a task exists in Done
When the user moves the task to To Do at position 0
Then To Do contains the task at position 0
And Done is empty
```

### 1.2 Move task between columns preserves other column

```gherkin
Given task "A" exists in To Do and task "B" exists in In Progress
When the user moves "A" to Done at position 0
Then In Progress still contains "B" at position 0
And To Do is empty
```

---

## 2. Reindex Edge Cases

### 2.1 Source column reindexes after task leaves

```gherkin
Given tasks "A", "B", "C" exist in To Do at positions 0, 1, 2
When the user moves "A" to In Progress at position 0
Then To Do contains "B" at position 0 and "C" at position 1
```

### 2.2 Target column shifts tasks down on insertion

```gherkin
Given tasks "X", "Y" exist in In Progress at positions 0, 1
When the user moves a task from To Do to In Progress at position 0
Then In Progress contains the moved task at 0, "X" at 1, "Y" at 2
```

---

## 3. Position Edge Cases

### 3.1 Move to position 0 in empty column

```gherkin
Given In Progress is empty
When the user moves a task from To Do to In Progress at position 0
Then In Progress contains only the moved task at position 0
```

### 3.2 Reorder last task to first position

```gherkin
Given tasks "A", "B", "C" exist in To Do
When the user moves "C" to To Do at position 0
Then To Do contains "C", "A", "B" in order
```

### 3.3 Reorder first task to last position

```gherkin
Given tasks "A", "B", "C" exist in To Do
When the user moves "A" to To Do at position 2
Then To Do contains "B", "C", "A" in order
```
