package com.example.controller.test;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!prod")
@RequestMapping("/api/test")
public class CleanupController {

    private final JdbcTemplate jdbcTemplate;

    public CleanupController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/cleanup")
    public ResponseEntity<Void> cleanup() {
        jdbcTemplate.execute("TRUNCATE TABLE tasks");
        return ResponseEntity.ok().build();
    }
}
