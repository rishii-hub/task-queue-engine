package com.taskqueue.task_queue_engine.job;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    @Transactional
    public Job createJob(CreateJobRequest request) {
        Job job = Job.builder()
                .type(request.getType())
                .payload(request.getPayload())
                .priority(request.getPriority())
                .maxAttempts(request.getMaxAttempts())
                .runAt(request.getRunAt() != null ? request.getRunAt() : OffsetDateTime.now())
                .status(JobStatus.PENDING)
                .build();

        return jobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public Job getJob(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));
    }
}