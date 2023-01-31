package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends Throwable {
    private String message;

    public ValidationException(){};

    public ValidationException(String message){
        this.message = message;
    };

    @Override
    public String getMessage(){
        return message;
    }
}
