package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rate;
import ru.yandex.practicum.filmorate.storage.rate.RateStorage;

import java.util.List;

@Slf4j
@Service
public class RateService {

    private final RateStorage rateStorage;

    @Autowired
    public RateService(RateStorage rateStorage) {
        this.rateStorage = rateStorage;
    }

    public List<Rate> getAllRates() {
        return rateStorage.getAllRates();
    }

    public Rate getRateById(int id) {
        return rateStorage.getRateById(id);
    }
}