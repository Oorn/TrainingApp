package org.example.exceptions;

public class NoPermissionException  extends RuntimeException{
    public NoPermissionException(String message) {
        super(message);
    }
}
