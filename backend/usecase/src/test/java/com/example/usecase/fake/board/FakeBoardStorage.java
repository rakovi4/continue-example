package com.example.usecase.fake.board;

import com.example.domain.board.Board;
import com.example.domain.task.Task;
import com.example.usecase.board.BoardStorage;

import java.util.ArrayList;
import java.util.List;

public class FakeBoardStorage implements BoardStorage {

    private final List<Task> savedTasks = new ArrayList<>();

    private Board board;

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void saveTask(Task task) {
        savedTasks.add(task);
    }

    public List<Task> getSavedTasks() {
        return List.copyOf(savedTasks);
    }
}
