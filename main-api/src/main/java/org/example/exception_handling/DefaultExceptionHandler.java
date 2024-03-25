package org.example.exception_handling;

import org.example.exceptions.BadRequestException;
import org.example.exceptions.NoPermissionException;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException e) {

        return new ResponseEntity<>(Collections.singletonMap("bad request: ", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException e) {

        return new ResponseEntity<>(Collections.singletonMap("general error: ", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<Object> handleNoPermission(NoPermissionException e) {

        return new ResponseEntity<>(Collections.singletonMap("not permitted: ", e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<Object> handleNoSuchEntity(NoSuchEntityException e) {

        return new ResponseEntity<>(Collections.singletonMap("not found: ", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RemovedEntityException.class)
    public ResponseEntity<Object> handleRemovedEntity(RemovedEntityException e) {

        return new ResponseEntity<>(Collections.singletonMap("removed: ", e.getMessage()), HttpStatus.GONE);
    }
}
