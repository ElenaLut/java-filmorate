package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String s) {
        super(s);
    }
}