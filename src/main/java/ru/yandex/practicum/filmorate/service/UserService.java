package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(@RequestBody User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        userStorage.validateUserId(id);
        return userStorage.getUsers().get(id);
    }

    public List<User> getUserFriends(long id) {
        userStorage.validateUserId(id);
        List<User> friends = new ArrayList<>();
        for (long i : userStorage.getUsers().get(id).getFriends()) {
            friends.add(userStorage.getUsers().get(i));
        }
        return friends;
    }

    public void addInFriendList(long userFirstId, long userSecondId) {
        log.info("Запрос на добавление в друзья пользователей id={} и id={} направлен", userFirstId, userSecondId);
        User userFirst = userStorage.getUserById(userFirstId);
        User userSecond = userStorage.getUserById(userSecondId);
        userStorage.validateUserId(userFirstId);
        userStorage.validateUserId(userSecondId);
        validateUsersIsFriends(userFirstId, userSecondId);
        validateUsersIsFriends(userSecondId, userFirstId);
        userFirst.getFriends().add(userSecondId);
        userSecond.getFriends().add(userFirstId);
        userStorage.updateUser(userFirst);
        userStorage.updateUser(userSecond);
        log.info("Пользователи id={} и id={} добавлены в друзья", userFirstId, userSecondId);
    }

    public void validateUsersIsFriends(long userFirstId, long userSecondId) {
        if (userStorage.getUsers().get(userFirstId).getFriends().contains(userSecondId)) {
            log.error("Пользователь id={} уже добавлен в друзья пользователя id={}", userSecondId, userFirstId);
            throw new ValidationException("Пользователь id=" + userSecondId + " уже добавлен в друзья пользователя " +
                    "id=" + userFirstId);
        }
    }

    public void validateUsersIsNotFriends(long userFirstId, long userSecondId) {
        if (!userStorage.getUsers().get(userFirstId).getFriends().contains(userSecondId)) {
            log.error("Пользователь id={} не добавлен в друзья пользователя id={}", userSecondId, userFirstId);
            throw new ObjectNotFoundException("Пользователь id=" + userSecondId + " не добавлен в друзья " +
                    "пользователя " + userFirstId);
        }
    }

    public void removeFriendList(long userFirstId, long userSecondId) {
        log.info("Запрос на удаление из друзей пользователей id={} и id={} направлен", userFirstId, userSecondId);
        User userFirst = userStorage.getUserById(userFirstId);
        User userSecond = userStorage.getUserById(userSecondId);
        userStorage.validateUserId(userFirstId);
        userStorage.validateUserId(userSecondId);
        validateUsersIsNotFriends(userFirstId, userSecondId);
        validateUsersIsNotFriends(userSecondId, userFirstId);
        userFirst.getFriends().remove(userSecondId);
        userSecond.getFriends().remove(userFirstId);
        userStorage.updateUser(userFirst);
        userStorage.updateUser(userSecond);
        log.info("Пользователи id={} и id={} удалены из друзей", userFirstId, userSecondId);
    }

    public List<User> showCommonFriends(long userFirstId, long userSecondId) {
        log.info("Запрос общих друзей пользователей id={} и id={} направлен", userFirstId, userSecondId);
        User userFirst = userStorage.getUserById(userFirstId);
        User userSecond = userStorage.getUserById(userSecondId);
        userStorage.validateUserId(userFirstId);
        userStorage.validateUserId(userSecondId);
        validateUsersIsNotFriends(userFirstId, userSecondId);
        validateUsersIsNotFriends(userSecondId, userFirstId);
        List<User> commonUser = new ArrayList<>();
        for (User user : userStorage.getUsers().values()) {
            if (userFirst.getFriends().contains(user.getId()) &
                    userSecond.getFriends().contains(user.getId())) {
                commonUser.add(user);
            }
        }
        return commonUser;
    }
}