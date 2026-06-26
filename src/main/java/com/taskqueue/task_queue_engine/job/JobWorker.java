package com.taskqueue.task_queue_engine.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobWorker {

    private final JobRepository jobRepository;

    @Transactional
    public void pollAndProcess() {
        List<Job> jobs = jobRepository.findAndLockPendingJobs(OffsetDateTime.now(), 1);
        if (jobs.isEmpty()) return;

        Job job = jobs.get(0);
        log.info("[Worker] Claimed job id={} type={}", job.getId(), job.getType());

        job.setStatus(JobStatus.RUNNING);
        job.setStartedAt(OffsetDateTime.now());
        job.setAttempts(job.getAttempts() + 1);
        jobRepository.save(job);

        try {
            processJob(job);
            job.setStatus(JobStatus.DONE);
            job.setCompletedAt(OffsetDateTime.now());
            log.info("[Worker] Job id={} DONE", job.getId());
        } catch (Exception e) {
            log.error("[Worker] Job id={} FAILED: {}", job.getId(), e.getMessage());
            job.setErrorMessage(e.getMessage());

            if (job.getAttempts() >= job.getMaxAttempts()) {
                job.setStatus(JobStatus.FAILED);
                log.warn("[Worker] Job id={} exhausted retries, marked FAILED", job.getId());
            } else {
                // Reset to PENDING — exponential backoff handled in Day 3
                job.setStatus(JobStatus.PENDING);
                job.setRunAt(OffsetDateTime.now().plusSeconds(
                        (long) Math.pow(2, job.getAttempts()) // 2s, 4s, 8s...
                ));
                log.info("[Worker] Job id={} scheduled retry at {}", job.getId(), job.getRunAt());
            }
        }

        jobRepository.save(job);
    }

    private void processJob(Job job) throws Exception {
        // Placeholder — simulates real work
        // Day 4+: dispatch to a handler registry based on job.getType()
        log.info("[Worker] Processing type={} payload={}", job.getType(), job.getPayload());
        Thread.sleep(300);
    }
}