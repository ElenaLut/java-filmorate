package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    Map<Long, Film> getFilms();

    Film getFilmById(long id);

    void validateFilmId(long id);

    void validateUniqName(Film film);
}