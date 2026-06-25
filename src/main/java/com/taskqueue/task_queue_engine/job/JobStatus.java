package com.taskqueue.task_queue_engine.job;

public enum JobStatus {
    PENDING,   // Waiting to be picked up
    RUNNING,   // Currently being processed by a worker
    DONE,      // Completed successfully
    FAILED     // Exhausted all retry attempts
}
