package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    User addUser(@RequestBody User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    Map<Long, User> getUsers();

    User getUserById(long id);

    void validateUserId(long id);

    void validateUniqEmail(User user);
}