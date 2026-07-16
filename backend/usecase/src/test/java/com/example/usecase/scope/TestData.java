package com.example.usecase.scope;

import java.time.Instant;

public final class TestData {

    public static final Instant NOW = Instant.parse("2026-07-16T10:15:30.123456789Z");
    public static final Instant CREATED_AT = Instant.parse("2026-07-16T10:15:30.123Z");

    private TestData() {
    }
}
