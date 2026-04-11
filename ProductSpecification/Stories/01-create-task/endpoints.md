# Create Task - API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | /api/v1/board | Get board with columns and tasks |
| POST | /api/v1/tasks | Create a new task in To Do column |

## Notes

- GET /api/v1/board returns the full board state (columns + tasks ordered by position)
- POST /api/v1/tasks returns 201 with the created task; 400 on validation or duplicate title
- No authentication — single-user app
