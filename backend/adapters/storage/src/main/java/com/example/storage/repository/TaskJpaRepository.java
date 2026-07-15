package com.example.storage.repository;

import com.example.storage.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
}
