package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    User addUser(@RequestBody User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    void validateUniqEmail(User user);

    void validateNewUser(User user);

    void validateUserId(long id);

    Map<Long, User> getUsers();
}