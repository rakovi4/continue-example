package com.example.storage.access.board;

import com.example.domain.board.ColumnType;
import com.example.domain.task.Description;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("3.1 Create task with title only")
class H2BoardStorageSaveTaskTest extends AbstractBoardStorageTest {

    private static final Instant CREATED_AT = Instant.parse("2026-07-16T10:15:30.123Z");
    private static final UUID NEW_TASK_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID FIRST_NEIGHBOUR_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID SECOND_NEIGHBOUR_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("""
            When the user creates a task with title "Set up CI/CD"
            Then the task is created successfully
            And the response contains the task with title "Set up CI/CD" and no description
            And the task has a position and creation timestamp""")
    void should_save_title_only_task_and_read_it_back_from_to_do() {
        Task titleOnlyTask = new Task(NEW_TASK_ID, new Title("Set up CI/CD"), null, 1, CREATED_AT);

        h2BoardStorage.saveTask(titleOnlyTask);
        detachPersistedEntities();

        assertThat(toDoTasks())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(titleOnlyTask);
    }

    @Test
    @DisplayName("""
            When the user creates a task with title "Set up CI/CD" and description "Configure GitHub Actions"
            Then every field survives the round trip through the database
            And the creation timestamp keeps millisecond precision""")
    void should_round_trip_every_task_field_including_created_at_millis() {
        Task task = new Task(
                NEW_TASK_ID,
                new Title("Set up CI/CD"),
                new Description("Configure GitHub Actions"),
                1,
                CREATED_AT
        );

        h2BoardStorage.saveTask(task);
        detachPersistedEntities();

        assertThat(toDoTasks())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(task);
    }

    @Test
    @DisplayName("""
            Given tasks "First task" and "Second task" exist in To Do
            When the user creates a task with title "Set up CI/CD"
            Then the To Do column contains all three tasks
            And the two existing tasks are unchanged""")
    void should_insert_new_task_without_touching_existing_tasks() {
        Task firstNeighbour = new Task(
                FIRST_NEIGHBOUR_ID,
                new Title("First task"),
                new Description("First description"),
                1,
                CREATED_AT
        );
        Task secondNeighbour = new Task(
                SECOND_NEIGHBOUR_ID,
                new Title("Second task"),
                new Description("Second description"),
                2,
                Instant.parse("2026-07-16T10:15:31.456Z")
        );
        h2BoardStorage.saveTask(firstNeighbour);
        h2BoardStorage.saveTask(secondNeighbour);
        detachPersistedEntities();

        Task titleOnlyTask = new Task(
                NEW_TASK_ID,
                new Title("Set up CI/CD"),
                null,
                3,
                Instant.parse("2026-07-16T10:15:32.789Z")
        );
        h2BoardStorage.saveTask(titleOnlyTask);
        detachPersistedEntities();

        assertThat(toDoTasks())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(firstNeighbour, secondNeighbour, titleOnlyTask);
    }

    private void detachPersistedEntities() {
        entityManager.flush();
        entityManager.clear();
    }

    private List<Task> toDoTasks() {
        return h2BoardStorage.getBoard().getColumns().stream()
                .filter(column -> column.getType() == ColumnType.TO_DO)
                .findFirst()
                .orElseThrow()
                .getTasks();
    }
}
