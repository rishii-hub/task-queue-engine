package com.taskqueue.task_queue_engine.job;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter @Setter
public class CreateJobRequest {

    @NotBlank(message = "Job type is required")
    private String type;

    private Map<String, Object> payload;

    @Min(0) @Max(100)
    private int priority = 0;

    @Min(1) @Max(10)
    private int maxAttempts = 3;

    private OffsetDateTime runAt;
}