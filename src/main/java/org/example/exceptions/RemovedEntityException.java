package org.example.exceptions;

public class RemovedEntityException extends RuntimeException {
    public RemovedEntityException(String message) {
        super(message);
    }
}
