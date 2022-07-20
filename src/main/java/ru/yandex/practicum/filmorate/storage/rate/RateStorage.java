package ru.yandex.practicum.filmorate.storage.rate;

import ru.yandex.practicum.filmorate.model.Rate;

import java.util.List;

public interface RateStorage {

    List<Rate> getAllRates();

    Rate getRateById(int id);
}