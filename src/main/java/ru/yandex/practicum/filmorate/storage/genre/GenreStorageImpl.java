package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreStorageImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

   /* @Override
    public List<Genre> getAllGenres() {
        final String query = "SELECT * FROM genre";
        return jdbcTemplate.queryForStream(query, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("genre_name"))).collect(Collectors.toList());
    }

    @Override
    public Optional<Genre> getGenreById(long id) throws NotFoundException {
        final String query = "SELECT * FROM genre WHERE genre_id =  ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(query, id);
        if (rs.next()) {
            return Optional.of(new Genre(rs.getInt(1), rs.getString(2)));
        }
        return Optional.empty();
    }*/
   @Override
   public Genre getGenreById(long id) {
       /*String sql = "SELECT * FROM genre WHERE genre_id = ?;";
       Genre genre;
       try {
           genre = jdbcTemplate.queryForObject(sql, (resultSet, rowId) -> buildGenre(resultSet), id);
       } catch(IncorrectResultSizeDataAccessException e) {
           throw new NotFoundException("Такойго id не существует");
       }
       return genre;*/
       final String query = "SELECT * FROM genre WHERE genre_id =  ?";
       List<Genre> genres = jdbcTemplate.query(query, (rs, rowNum) -> buildGenre(rs), id);
       if (genres.isEmpty()) {
           throw new NotFoundException("Такого рейтинга не существует");
       }
       return genres.get(0);
   }

    @Override
    public List<Genre> getAllGenres() {
       String query = "SELECT * FROM genre;";
        List<Genre> genres = jdbcTemplate.query(query, (rs, rowNum) -> buildGenre(rs));
        return genres;
        //return Arrays.asList(GenreName.values());
    }

    private Genre buildGenre(ResultSet resultSet) throws SQLException {
        return new Genre((int) resultSet.getLong("genre_id"),
                resultSet.getString("genre_name"));
    }
}