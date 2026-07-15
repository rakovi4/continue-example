package com.example.domain.board;

public enum ColumnType {
    TO_DO,
    IN_PROGRESS,
    DONE;

    public String value() {
        return switch (this) {
            case TO_DO -> "To Do";
            case IN_PROGRESS -> "In Progress";
            case DONE -> "Done";
        };
    }

}
