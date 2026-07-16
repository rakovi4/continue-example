# Story 1: Create Task — Progress

## Spec
- [x] interview
- [x] story
- [x] mockups
- [x] api-spec
- [x] test-spec

## Backend Scenarios (01_API_Tests.md)

### 1.1 Get empty board
- [x] red-acceptance
- [x] design (see ADR: board-domain-model-decision.md)
- [x] red-usecase
- [x] green-usecase
- [x] adapters-discovery (BoardStorage → rest, storage)
- [x] red-adapter rest
- [x] green-adapter rest
- [x] red-adapter storage
- [x] green-adapter storage
- [x] green-acceptance

### 2.1 Reject empty title
- [x] red-acceptance
- [x] design (see ADR: validation-pattern-decision.md)
- [x] red-usecase
- [x] green-usecase
- [x] red-usecase (coverage: Title rejects null value)
- [S] green-usecase (coverage: Title rejects null value — code already handles null, test passes immediately)
- [x] adapters-discovery (rest only — validation rejects before persistence)
- [x] red-adapter rest
- [x] green-adapter rest
- [S] red-adapter storage (no storage port — validation rejects before persistence)
- [S] green-adapter storage (no storage port — validation rejects before persistence)
- [x] green-acceptance

### 2.2 Reject title exceeding 100 characters
- [x] red-acceptance
- [x] design (see ADR: validation-pattern-decision.md)
- [x] red-usecase
- [x] green-usecase
- [x] adapters-discovery (none — validation handler exists from 2.1)
- [S] red-adapter rest (no new REST behavior — validation handler exists from 2.1)
- [S] green-adapter rest (no new REST behavior — validation handler exists from 2.1)
- [S] red-adapter storage (no storage port — validation rejects before persistence)
- [S] green-adapter storage (no storage port — validation rejects before persistence)
- [x] green-acceptance

### 2.3 Reject description exceeding 5000 characters
- [x] red-acceptance
- [x] design (see ADR: validation-pattern-decision.md)
- [x] red-usecase
- [x] green-usecase
- [x] adapters-discovery (rest only — validation rejects before persistence)
- [x] red-adapter rest
- [x] green-adapter rest
- [S] red-adapter storage (no storage port — validation rejects before persistence)
- [S] green-adapter storage (no storage port — validation rejects before persistence)
- [x] green-acceptance

### 3.1 Create task with title only
- [x] red-acceptance
- [x] design (see ADR: task-id-strategy-decision.md)
- [x] red-usecase
- [x] green-usecase
- [x] design (see ADR: board-aggregate-mutation-decision.md — refactor to Board aggregate)
- [x] refactor-usecase (Board aggregate: eliminate TaskStorage, Board.addTask() owns position + column)
- [x] red-usecase (coverage: Description rejects null value)
- [S] green-usecase (coverage: Description rejects null value — production code already handles null, test passes in RED)
- [x] adapters-discovery (BoardStorage.save replaces TaskStorage)
- [x] red-adapter storage
- [x] green-adapter storage
- [x] red-adapter rest
- [x] green-adapter rest
- [x] green-acceptance

### 3.2 Create task with title and description
- [x] red-acceptance
- [S] design (feature fully implemented in 3.1 — description already supported)
- [x] red-usecase (retroactive: add usecase-level coverage for description persistence)
- [S] green-usecase (retroactive: production code already implemented in 3.1, test green on first run)
- [S] adapters-discovery (feature fully implemented in 3.1)
- [x] green-acceptance

### 4.1 Get board with tasks in To Do
- [x] red-acceptance
- [S] design (board GET already returns tasks from scenario 3.1)
- [x] red-usecase (retroactive: add usecase-level coverage for board GET returning tasks)
- [S] green-usecase (retroactive: production code already implemented, test green on first run)
- [S] adapters-discovery (feature fully implemented in 3.1)
- [x] green-acceptance

### 4.2 New task appears at bottom of To Do column
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

### 5.1 Reject duplicate task title
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

## Frontend Scenarios (02_UI_Tests.md)

### 1.1 Display empty board with three columns
- [ ] red-selenium
- [ ] red-frontend
- [ ] green-frontend
- [ ] red-frontend-api
- [ ] green-frontend-api
- [ ] align-design
- [ ] green-selenium
- [ ] demo

### 2.1 Display task creation form
- [ ] red-selenium
- [ ] red-frontend
- [ ] green-frontend
- [ ] red-frontend-api
- [ ] green-frontend-api
- [ ] align-design
- [ ] green-selenium
- [ ] demo

### 3.1 Submit task with title and description
- [ ] red-selenium
- [ ] red-frontend
- [ ] green-frontend
- [ ] red-frontend-api
- [ ] green-frontend-api
- [ ] align-design
- [ ] green-selenium
- [ ] demo

### 4.1 Display validation error for empty title
- [ ] red-selenium
- [ ] red-frontend
- [ ] green-frontend
- [ ] red-frontend-api
- [ ] green-frontend-api
- [ ] align-design
- [ ] green-selenium
- [ ] demo

### 4.2 Display duplicate title error
- [ ] red-selenium
- [ ] red-frontend
- [ ] green-frontend
- [ ] red-frontend-api
- [ ] green-frontend-api
- [ ] align-design
- [ ] green-selenium
- [ ] demo

## Security Scenarios (05_Security_Tests.md)

### 1.1 SQL injection via task title and description
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

### 1.2 XSS via task title and description
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

### 2.1 Reject extra fields in task creation request
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance

### 3.1 Server enforces field length limits
- [ ] red-acceptance
- [ ] design
- [ ] red-usecase
- [ ] green-usecase
- [ ] adapters-discovery
- [ ] green-acceptance
