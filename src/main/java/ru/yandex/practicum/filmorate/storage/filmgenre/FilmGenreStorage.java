package ru.yandex.practicum.filmorate.storage.filmgenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

public interface FilmGenreStorage {
    LinkedHashSet<Genre> findAllByFilmId(long id);

    void addGenreToFilm(long id, Genre genre);

    void updateGenresToFilm(Film film);
}