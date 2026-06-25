package com.taskqueue.task_queue_engine.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    // Spring Data JPA generates the SQL for save(), findById(), findAll() etc.
    // We'll add custom @Query methods in later days for SELECT FOR UPDATE SKIP LOCKED
}