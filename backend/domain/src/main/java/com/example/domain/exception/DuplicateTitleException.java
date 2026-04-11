package com.example.domain.exception;

public class DuplicateTitleException extends RuntimeException {

    public DuplicateTitleException() {
        super("A task with this title already exists");
    }
}
