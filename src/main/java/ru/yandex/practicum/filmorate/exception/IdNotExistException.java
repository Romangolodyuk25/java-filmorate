package ru.yandex.practicum.filmorate.exception;

public class IdNotExistException extends RuntimeException {
    public IdNotExistException(String mess) {
        super(mess);
    }
}
