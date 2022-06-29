package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Component;

@Component
public class GeneratorUserId {
    private long id = 0;

    public long generate() {
        return ++id;
    }
}