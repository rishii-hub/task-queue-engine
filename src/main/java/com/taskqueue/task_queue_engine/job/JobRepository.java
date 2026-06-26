package com.taskqueue.task_queue_engine.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    // The core of the engine — atomically claims jobs, no duplicates, no blocking
    @Query(value = """
            SELECT * FROM jobs
            WHERE status = 'PENDING'
            AND run_at <= :now
            ORDER BY priority DESC, created_at ASC
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<Job> findAndLockPendingJobs(
            @Param("now") OffsetDateTime now,
            @Param("limit") int limit
    );

    // Used by the watchdog (Day 3) to detect stalled jobs
    @Query(value = """
            SELECT * FROM jobs
            WHERE status = 'RUNNING'
            AND started_at < :cutoff
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<Job> findStalledJobs(@Param("cutoff") OffsetDateTime cutoff);
}