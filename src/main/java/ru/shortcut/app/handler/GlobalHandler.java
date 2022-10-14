package ru.shortcut.app.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.shortcut.app.ex.ForbiddenException;
import ru.shortcut.app.ex.NoSuchValueException;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> forbiddenExceptionHandler(ForbiddenException forbiddenException) {
        return ResponseEntity.status(forbiddenException.getHttpStatus())
                .body(forbiddenException.getMessage());
    }

    @ExceptionHandler(NoSuchValueException.class)
    public ResponseEntity<Void> notFountException() {
        return ResponseEntity.notFound().build();
    }
}
