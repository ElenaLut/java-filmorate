package ru.yandex.practicum.filmorate.storage.rate;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rate;
import ru.yandex.practicum.filmorate.model.RateName;

import java.util.List;

public interface RateStorage {

    List<RateName> getAllRates();

    RateName getRateById(int id);
}