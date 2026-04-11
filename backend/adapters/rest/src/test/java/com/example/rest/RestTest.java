package com.example.rest;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public interface RestTest {

    default void assertTimestampIsNow(MvcResult result) throws Exception {
        Instant timestamp = Instant.parse(
                JsonPath.read(result.getResponse().getContentAsString(), "$.timestamp")
        );
        assertThat(timestamp.truncatedTo(ChronoUnit.MINUTES))
                .as("error timestamp")
                .isEqualTo(Instant.now().truncatedTo(ChronoUnit.MINUTES));
    }
}
