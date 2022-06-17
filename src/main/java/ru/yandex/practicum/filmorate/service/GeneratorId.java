package ru.yandex.practicum.filmorate.service;

public class GeneratorId {
    private long id = 0;

    public long generate() {
        return ++id;
    }
}