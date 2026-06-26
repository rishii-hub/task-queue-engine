package com.taskqueue.task_queue_engine.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobWorker jobWorker;

    // 3 concurrent workers — each will grab a different job via SKIP LOCKED
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    @Scheduled(fixedDelay = 1000)
    public void poll() {
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                try {
                    jobWorker.pollAndProcess();
                } catch (Exception e) {
                    log.error("[Scheduler] Worker threw: {}", e.getMessage());
                }
            });
        }
    }
}