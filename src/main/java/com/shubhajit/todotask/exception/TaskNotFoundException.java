package com.shubhajit.todotask.exception;

/**
 * Exception thrown when a task is not found in the system.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
