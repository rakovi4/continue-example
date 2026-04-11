package com.example.acceptance.clients.application.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSummaryResponse {
    private String id;
    private ValueWrapper title;
    private ValueWrapper description;
    private int position;
    private String createdAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValueWrapper {
        private String value;
    }

    public String getTitleValue() {
        return title != null ? title.getValue() : null;
    }

    public String getDescriptionValue() {
        return description != null ? description.getValue() : null;
    }
}
