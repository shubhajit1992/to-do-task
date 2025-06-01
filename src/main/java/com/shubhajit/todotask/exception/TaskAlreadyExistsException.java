package com.shubhajit.todotask.exception;

/**
 * Exception thrown when attempting to create a duplicate task.
 */
public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
