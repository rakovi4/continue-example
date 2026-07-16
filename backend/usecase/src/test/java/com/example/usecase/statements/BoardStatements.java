package com.example.usecase.statements;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.board.ColumnType;
import com.example.domain.task.Task;
import com.example.usecase.board.GetBoardUseCase;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class BoardStatements {

    private final GetBoardUseCase getBoardUseCase;

    private Board boardResult;

    public void getBoard() {
        boardResult = getBoardUseCase.getBoard();
    }

    public void assertBoardHasThreeEmptyColumns() {
        assertThat(boardResult.getColumns())
                .as("board columns")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        Column.empty(ColumnType.TO_DO),
                        Column.empty(ColumnType.IN_PROGRESS),
                        Column.empty(ColumnType.DONE)
                );
    }

    public void assertBoardHasOnlyTaskInToDo(String expectedTitle) {
        assertOnlyToDoColumnHasTasks();
        assertSingleTaskInToDo(expectedTitle);
    }

    private void assertOnlyToDoColumnHasTasks() {
        assertThat(boardResult.getColumns())
                .as("board columns")
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("tasks")
                .containsExactly(
                        Column.empty(ColumnType.TO_DO),
                        Column.empty(ColumnType.IN_PROGRESS),
                        Column.empty(ColumnType.DONE)
                );
        assertThat(boardResult.getColumns().get(1).getTasks()).as("In Progress tasks").isEmpty();
        assertThat(boardResult.getColumns().get(2).getTasks()).as("Done tasks").isEmpty();
    }

    private void assertOrderedTask(Task task, String expectedTitle, int expectedPosition) {
        assertValidUuid(task.getId());
        assertThat(task.getTitle().getValue()).as("task title").isEqualTo(expectedTitle);
        assertThat(task.getDescription().getValue()).as("task description").isEqualTo("");
        assertThat(task.getPosition()).as("task position").isEqualTo(expectedPosition);
        assertRecentTimestamp(task.getCreatedAt());
    }

    private void assertValidUuid(UUID id) {
        assertThat(id).as("task id").isNotNull();
        assertThat(id.toString()).as("task id format")
                .matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    public void assertToDoColumnContainsTasksInOrder(String firstTitle, String secondTitle) {
        assertOnlyToDoColumnHasTasks();
        var toDoTasks = boardResult.getColumns().get(0).getTasks();
        assertThat(toDoTasks).as("To Do tasks").hasSize(2);
        assertOrderedTask(toDoTasks.get(0), firstTitle, 0);
        assertOrderedTask(toDoTasks.get(1), secondTitle, 1);
    }

    private void assertSingleTaskInToDo(String expectedTitle) {
        var toDoTasks = boardResult.getColumns().get(0).getTasks();
        assertThat(toDoTasks).as("To Do tasks").hasSize(1);
        assertOrderedTask(toDoTasks.get(0), expectedTitle, 0);
    }

    private void assertRecentTimestamp(Instant timestamp) {
        assertThat(timestamp).as("task creation timestamp").isBetween(
                Instant.now().minusSeconds(30), Instant.now().plusSeconds(30));
    }
}
