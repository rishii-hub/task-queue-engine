package com.taskqueue.task_queue_engine.job;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // The type identifies which worker handler to invoke (e.g. "send_email", "process_payment")
    @Column(nullable = false)
    private String type;

    // JSONB in Postgres — arbitrary key/value data specific to this job type
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @Enumerated(EnumType.STRING)  // Stores "PENDING" not "0" — readable in DB
    @Column(nullable = false)
    @Builder.Default
    private JobStatus status = JobStatus.PENDING;

    // Higher number = higher priority. Default 0 means normal priority.
    @Column(nullable = false)
    @Builder.Default
    private Integer priority = 0;

    // How many times this job has been attempted
    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    // Max retries before marking as FAILED
    @Column(nullable = false)
    @Builder.Default
    private Integer maxAttempts = 3;

    // Jobs can be scheduled for the future by setting runAt ahead of now()
    @Column(nullable = false)
    @Builder.Default
    private OffsetDateTime runAt = OffsetDateTime.now();

    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private String errorMessage;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}