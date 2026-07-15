package com.example.acceptance.statements;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;

public final class AssertionHelpers {

    private AssertionHelpers() {
    }

    public static void assertTimestampRecent(String timestamp, String description) {
        assertThat(Instant.parse(timestamp).truncatedTo(MINUTES))
                .as(description)
                .isEqualTo(Instant.now().truncatedTo(MINUTES));
    }
}
