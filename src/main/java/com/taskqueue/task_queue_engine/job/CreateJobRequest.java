package com.taskqueue.task_queue_engine.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
public class CreateJobRequest {

    @NotBlank(message = "Job type is required")
    private String type;

    // Payload is optional — some job types might not need extra data
    private Map<String, Object> payload;

    @Min(value = 0, message = "Priority must be >= 0")
    @Max(value = 100, message = "Priority must be <= 100")
    private int priority = 0;

    @Min(value = 1) @Max(value = 10)
    private int maxAttempts = 3;

    // Null means "run now"; a future timestamp schedules the job
    private OffsetDateTime runAt;
}