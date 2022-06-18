package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

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
        User user = userStorage.getUserById(id);
        return user.getFriends().stream().map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }

    public void addInFriendList(long userFirstId, long userSecondId) {
        log.info("Запрос на добавление в друзья пользователей id={} и id={} направлен", userFirstId, userSecondId);
        User userFirst = userStorage.getUserById(userFirstId);
        User userSecond = userStorage.getUserById(userSecondId);
        userStorage.validateUserId(userFirstId);
        userStorage.validateUserId(userSecondId);
        userFirst.getFriends().add(userSecondId);
        userSecond.getFriends().add(userFirstId);
        userStorage.updateUser(userFirst);
        userStorage.updateUser(userSecond);
        log.info("Пользователи id={} и id={} добавлены в друзья", userFirstId, userSecondId);
    }

    public void removeFriendList(long userFirstId, long userSecondId) {
        log.info("Запрос на удаление из друзей пользователей id={} и id={} направлен", userFirstId, userSecondId);
        User userFirst = userStorage.getUserById(userFirstId);
        User userSecond = userStorage.getUserById(userSecondId);
        userStorage.validateUserId(userFirstId);
        userStorage.validateUserId(userSecondId);
        userFirst.getFriends().remove(userSecondId);
        userSecond.getFriends().remove(userFirstId);
        userStorage.updateUser(userFirst);
        userStorage.updateUser(userSecond);
        log.info("Пользователи id={} и id={} удалены из друзей", userFirstId, userSecondId);
    }

    public List<User> showCommonFriends(Long userFirstId, Long userSecondId) {
        User userFirst = userStorage.getUserById(userFirstId);
        if (userFirst == null) {
            throw new ObjectNotFoundException("пользователь с id=" + userFirstId + " Не найден");
        }
        User userSecond = userStorage.getUserById(userSecondId);
        if (userSecond == null) {
            throw new ObjectNotFoundException("пользователь с id=" + userSecondId + " Не найден");
        }
        Set<Long> commonFriends = new HashSet(userFirst.getFriends());
        commonFriends.retainAll(userSecond.getFriends());
        return commonFriends.stream().map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }
}