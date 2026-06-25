CREATE TABLE jobs (
                      id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      type        VARCHAR(100)  NOT NULL,
                      payload     JSONB,
                      status      VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
                      priority    INTEGER       NOT NULL DEFAULT 0,
                      attempts    INTEGER       NOT NULL DEFAULT 0,
                      max_attempts INTEGER      NOT NULL DEFAULT 3,
                      run_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                      started_at  TIMESTAMP WITH TIME ZONE,
                      completed_at TIMESTAMP WITH TIME ZONE,
                      error_message TEXT,
                      created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                      updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- This index is the core of the whole system.
-- When the worker polls for jobs, it filters by status='PENDING' and orders by priority + run_at.
-- Without this index, that query does a full table scan. With millions of jobs, that's unusable.
CREATE INDEX idx_jobs_status_priority_run_at
    ON jobs (status, priority DESC, run_at ASC)
    WHERE status = 'PENDING';