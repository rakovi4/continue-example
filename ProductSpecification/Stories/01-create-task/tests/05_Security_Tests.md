> **Implementation Order**: Tests are numbered for sequential TDD implementation.

## 1. Input Validation

### 1.1 SQL injection via task title and description

```gherkin
When the user creates a task with title "'; DROP TABLE tasks; --"
Then the task is created successfully with the literal title
And the database schema is intact
```

### 1.2 XSS via task title and description

```gherkin
When the user creates a task with title "<script>alert('xss')</script>"
Then the task is created successfully
When the user requests the board
Then the task title is returned escaped or as plain text
And no script is executable in the response
```

---

## 2. Mass Assignment

### 2.1 Reject extra fields in task creation request

```gherkin
When the user creates a task with title "Test" and extra fields id, position, and created_at
Then the task is created successfully
And the extra fields are ignored
And the task id, position, and created_at are server-generated
```

---

## 3. Input Length Enforcement

### 3.1 Server enforces field length limits

```gherkin
When the user creates a task with a title of exactly 100 characters
Then the task is created successfully
When the user creates a task with a title of 101 characters
Then the task is rejected with a validation error
When the user creates a task with a description of exactly 5000 characters
Then the task is created successfully
When the user creates a task with a description of 5001 characters
Then the task is rejected with a validation error
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `the user creates a task with title "X" and extra fields` | POST /api/v1/tasks with extra JSON fields |
| `the database schema is intact` | Subsequent queries succeed normally |
| `no script is executable in the response` | Response Content-Type is application/json, no HTML rendering |
| `the extra fields are ignored` | Response id, position, created_at differ from request values |
