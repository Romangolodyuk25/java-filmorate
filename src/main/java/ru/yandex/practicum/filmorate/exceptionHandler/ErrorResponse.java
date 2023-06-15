package ru.yandex.practicum.filmorate.exceptionHandler;

public class ErrorResponse {
    String message;
    String description;

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public ErrorResponse(String message, String description) {
        this.message = message;
        this.description = description;
    }
}
