> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with board display, then form display, then form submission, then validation feedback, then server responses.

## 1. Board Display

### 1.1 Display empty board with three columns

```gherkin
Given the user opens the board page
Then three columns are displayed: To Do, In Progress, Done
And each column is empty
And the Add Task button is visible
```

---

## 2. Task Creation Form

### 2.1 Display task creation form

```gherkin
Given the user opens the board page
When the user clicks the Add Task button
Then the task creation form is displayed
And the form contains a title field and a description field
And the submit button is visible
```

---

## 3. Form Submission

### 3.1 Submit task with title and description

```gherkin
Given the user opens the task creation form
When the user enters title "Set up CI/CD" and description "Configure GitHub Actions"
And the user submits the form
Then the task "Set up CI/CD" appears in the To Do column
```

---

## 4. Validation Feedback

### 4.1 Display validation error for empty title

```gherkin
Given the user opens the task creation form
When the user submits the form without entering a title
Then a validation error is displayed for the title field
```

### 4.2 Display duplicate title error

```gherkin
Given a task "Set up CI/CD" exists in To Do
And the user opens the task creation form
When the user enters title "Set up CI/CD"
And the user submits the form
Then an error message about duplicate title is displayed
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `the user opens the board page` | Navigate to app root URL |
| `three columns are displayed` | Elements with column names visible |
| `the Add Task button is visible` | Button with data-testid for add task |
| `the user clicks the Add Task button` | Click add task button |
| `the task creation form is displayed` | Form element with title and description fields visible |
| `the user enters title "X"` | Type into title input field |
| `the user enters title "X" and description "Y"` | Type into title and description fields |
| `the user submits the form` | Click submit button |
| `the task "X" appears in the To Do column` | Task card with title visible in To Do column |
| `a validation error is displayed for the title field` | Error message near title field visible |
| `an error message about duplicate title is displayed` | Error banner or inline error about duplicate |
