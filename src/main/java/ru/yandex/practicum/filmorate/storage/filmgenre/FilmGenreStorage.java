package ru.yandex.practicum.filmorate.storage.filmgenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface FilmGenreStorage {
    Set<Genre> findAllByFilmId(long id);

    void addGenreToFilm(long id, Genre genre);

    void updateGenresToFilm(Film film);
}