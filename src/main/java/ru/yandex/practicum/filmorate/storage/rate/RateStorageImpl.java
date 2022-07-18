package ru.yandex.practicum.filmorate.storage.rate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RateName;

import java.util.Arrays;
import java.util.List;

@Component
public class RateStorageImpl implements RateStorage{

    private final JdbcTemplate jdbcTemplate;

    public RateStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RateName> getAllRates() {
        return Arrays.asList(RateName.values());
    }

    @Override
    public RateName getRateById(int id) throws NotFoundException {
        return RateName.values()[id - 1];
    }
}