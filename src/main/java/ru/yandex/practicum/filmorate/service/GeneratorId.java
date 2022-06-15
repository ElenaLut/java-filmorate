package ru.yandex.practicum.filmorate.service;

public class GeneratorId {
    private int id = 0;

    public int generate() {
        return ++id;
    }
}