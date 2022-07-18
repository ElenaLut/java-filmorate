package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreName;

import java.util.List;

public interface GenreStorage {

    List<GenreName> getAllGenres();

    GenreName getGenreById(int id);
}