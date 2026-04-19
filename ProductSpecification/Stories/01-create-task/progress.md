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
- [x] design
- [x] red-usecase
- [x] green-usecase
- [x] red-adapter rest
- [x] green-adapter rest
- [x] red-adapter h2
- [x] green-adapter h2
- [x] green-acceptance

### 2.1 Reject empty title
- [x] red-acceptance
- [x] design (see ADR: validation-pattern-decision.md)
- [x] red-usecase
- [x] green-usecase
- [x] red-usecase (coverage: Title rejects null value)
- [S] green-usecase (coverage: Title rejects null value — code already handles null, test passes immediately)
- [x] red-adapter rest
- [x] green-adapter rest
- [S] red-adapter h2 (no storage port — validation rejects before persistence)
- [S] green-adapter h2 (no storage port — validation rejects before persistence)
- [x] green-acceptance

### 2.2 Reject title exceeding 100 characters
- [x] red-acceptance
- [x] design (see ADR: validation-pattern-decision.md)
- [x] red-usecase
- [x] green-usecase
- [S] red-adapter rest (no new REST behavior — validation handler exists from 2.1)
- [S] green-adapter rest (no new REST behavior — validation handler exists from 2.1)
- [S] red-adapter h2 (no storage port — validation rejects before persistence)
- [S] green-adapter h2 (no storage port — validation rejects before persistence)
- [x] green-acceptance

### 2.3 Reject description exceeding 5000 characters
- [x] red-acceptance
- [x] design
- [x] red-usecase
- [x] green-usecase
- [x] red-adapter rest
- [x] green-adapter rest
- [S] red-adapter h2 (no storage port — validation rejects before persistence)
- [S] green-adapter h2 (no storage port — validation rejects before persistence)
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
- [x] red-adapter h2
- [x] green-adapter h2
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
- [x] red-acceptance
- [S] design (feature fully implemented in 3.1 — task ordering by position already works)
- [x] red-usecase (retroactive: add usecase-level coverage for task ordering by position)
- [S] green-usecase (retroactive: production code already implemented, test green on first run)
- [S] adapters-discovery (feature fully implemented in 3.1)
- [x] green-acceptance

### 5.1 Reject duplicate task title
- [x] red-acceptance
- [x] design
- [x] red-usecase
- [x] green-usecase
- [x] adapters-discovery (BoardStorage sufficient; DuplicateTitleException unmapped in REST handler)
- [x] red-adapter rest
- [x] green-adapter rest
- [S] red-adapter h2 (no new storage port — duplicate check is domain-level)
- [S] green-adapter h2 (no new storage port — duplicate check is domain-level)
- [x] green-acceptance

## Frontend Scenarios (02_UI_Tests.md)

### 1.1 Display empty board with three columns
- [x] red-selenium
- [S] red-frontend (trivial: static column names, no logic/branching/transformation)
- [S] green-frontend (trivial: no logic to implement)
- [x] red-frontend-api
- [x] green-frontend-api
- [x] align-design
- [x] red-frontend (coverage: displayName maps known columns)
- [x] green-frontend (coverage: displayName maps known columns)
- [x] red-frontend (coverage: displayName falls back for unknown)
- [x] green-frontend (coverage: displayName falls back for unknown)
- [x] green-selenium
- [x] demo

### 2.1 Display task creation form
- [x] red-selenium
- [S] red-frontend (trivial: form visibility is component-level state toggle, no logic/branching)
- [S] green-frontend (trivial: no logic to implement)
- [S] red-frontend-api (no API call — form display is client-side only, submission is scenario 3.1)
- [S] green-frontend-api (no API call — form display is client-side only)
- [x] align-design
- [x] green-selenium
- [~] demo

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
