package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    Film getFilmById(long id);

    public void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getTheMostPopularFilms(long count);
}