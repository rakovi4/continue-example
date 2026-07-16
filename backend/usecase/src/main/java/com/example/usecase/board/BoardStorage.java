package com.example.usecase.board;

import com.example.domain.board.Board;
import com.example.domain.task.Task;

public interface BoardStorage {

    Board getBoard();

    void saveTask(Task task);
}
