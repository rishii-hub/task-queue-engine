# Task Queue Engine

A distributed task queue engine built with Java, Spring Boot, PostgreSQL and Flyway.

## Tech Stack

- Java
- Spring Boot
- PostgreSQL
- Spring Data JPA
- Flyway

## Current Features

- REST API for creating jobs
- PostgreSQL persistence
- Flyway database migrations
- Layered architecture (Controller → Service → Repository)

## Upcoming

- Worker thread pool
- SELECT ... FOR UPDATE SKIP LOCKED
- Retry mechanism
- Exponential backoff
- Watchdog recovery
