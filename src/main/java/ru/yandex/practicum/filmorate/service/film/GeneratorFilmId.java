package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class GeneratorFilmId {
    private long id = 0;

    public long generate() {
        return ++id;
    }
}