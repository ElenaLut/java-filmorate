package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendsStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        if (user == null) {
            throw new NotFoundException("Запрос пуст");
        }
        String newUser = "INSERT INTO users" +
                "(user_id, email, login, user_name, birthday) " +
                "VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(newUser,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user == null) {
            throw new NotFoundException("Запрос пуст");
        }
        String userChange = "UPDATE users " +
                "SET " +
                "email=?, " +
                "login=?, " +
                "user_name=?, " +
                "birthday=? " +
                "WHERE user_id=?";
        jdbcTemplate.update(userChange,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("Пользователь изменен: {}", user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeUser(rs.getLong("user_id"), rs));
    }

    @Override
    public User getUserById(long id) {
        String query = "SELECT * FROM users WHERE user_id=?";
        List<User> users = jdbcTemplate.query(query, (rs, rowNum) -> makeUser(id, rs), id);
        if (users.isEmpty()) {
            throw new NotFoundException("Объекта не существует");
        }
        return users.get(0);
    }

    @Override
    public Map<User, FriendsStatus> getUserFriends(long id) {
        String query = "SELECT " +
                "u.user_id, " +
                "email, " +
                "login, " +
                "user_name, " +
                "birthday, " +
                "f.status " +
                "FROM user u " +
                "INNER JOIN (SELECT user_id, friend_id, status " +
                " FROM friends " +
                " WHERE user_id = ?) AS f " +
                " ON u.user_id = uf.friend_id";
        Map<User, FriendsStatus> friends = new HashMap<>();
        jdbcTemplate.query(query, rs -> {
            do {
                User friend = makeUser(rs.getLong("user_id"), rs);
                FriendsStatus status = FriendsStatus.values()[rs.getInt("status")];
                friends.put(friend, status);
            } while (rs.next());
        }, id);
        return friends;
    }

    @Override
    public void addInFriendList(long userFirstId, long userSecondId) {
        String newFriend = "INSERT INTO friends" +
                "(user_id, friend_id, status) " +
                "VALUES(?, ?, ?)";
        jdbcTemplate.update(newFriend,
                userFirstId,
                userSecondId,
                FriendsStatus.CONFIRMED
        );
        jdbcTemplate.update(newFriend,
                userSecondId,
                userFirstId,
                FriendsStatus.UNCONFIRMED);
        log.info("Пользователи id={} и id={} добавлены в друзья", userFirstId, userSecondId);
    }

    @Override
    public void removeFriendList(long userFirstId, long userSecondId) throws NotFoundException {
        User user = getUserById(userFirstId);
        User friend = getUserById(userSecondId);
        if (user == null || friend == null) {
            throw new NotFoundException("Запрос пуст");
        }
        String query = "DELETE FROM friends WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(query, userFirstId, userSecondId);
        jdbcTemplate.update(query, userSecondId, userFirstId);
        log.info("Пользователи id={} и id={} удалены из друзей", userFirstId, userSecondId);
    }

    @Override
    public List<User> showCommonFriends(Long userFirstId, Long userSecondId) {
        User firstUser = getUserById(userFirstId);
        User secondUser = getUserById(userSecondId);
        if (firstUser == null || secondUser == null) {
            throw new NotFoundException("Запрос пуст");
        }
        String query = "SELECT * FROM users WHERE user_id IN (" +
                "SELECT friend_id FROM friends " +
                "WHERE user_id=? OR user_id=?)";
        List<User> commonFriends = jdbcTemplate.query(query, (rs, rowNum) ->
                makeUser(rs.getLong("user_id"), rs), userFirstId, userSecondId);
        return commonFriends;
    }

    private User makeUser(long user_id, ResultSet rs) throws SQLException {
        String name = rs.getString("user_name");
        String login = rs.getString("login");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        String email = rs.getString("email");
        return new User(user_id, email, login, name, birthday);
    }
}