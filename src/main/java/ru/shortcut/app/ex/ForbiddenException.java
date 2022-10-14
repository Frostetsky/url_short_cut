package ru.shortcut.app.ex;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    public ForbiddenException(String message) {
        super(message);
        this.httpStatus = HttpStatus.FORBIDDEN;
    }
}
