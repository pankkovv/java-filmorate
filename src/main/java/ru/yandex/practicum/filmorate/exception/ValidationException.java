package ru.yandex.practicum.filmorate.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationException extends Throwable {
    private String message;

    public ValidationException() {
    }

    public ValidationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
