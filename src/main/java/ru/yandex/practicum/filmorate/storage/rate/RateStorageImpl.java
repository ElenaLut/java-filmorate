package ru.yandex.practicum.filmorate.storage.rate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RateStorageImpl implements RateStorage {

    private final JdbcTemplate jdbcTemplate;

    public RateStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Rate> getAllRates() {
        String query = "SELECT * FROM rate;";
        List<Rate> rate = jdbcTemplate.query(query, (rs, rowNum) -> buildRate(rs));
        return rate;
    }

    @Override
    public Rate getRateById(int id) throws NotFoundException {
        final String query = "SELECT * FROM rate WHERE rate_id =  ?";
        List<Rate> rates = jdbcTemplate.query(query, (rs, rowNum) -> buildRate(rs), id);
        if (rates.isEmpty()) {
            throw new NotFoundException("Такого рейтинга не существует");
        }
        return rates.get(0);
    }

    private Rate buildRate(ResultSet resultSet) throws SQLException {
        return new Rate((int) resultSet.getLong("rate_id"),
                resultSet.getString("rate_type"));
    }
}