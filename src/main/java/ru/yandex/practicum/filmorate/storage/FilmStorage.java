package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    void validateUniqName(Film film);

    void validateNewFilm(Film film);

    void validateFilmId(long id);

    Map<Long, Film> getFilms();
}