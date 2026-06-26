package com.taskqueue.task_queue_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskQueueEngineApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskQueueEngineApplication.class, args);
	}
}