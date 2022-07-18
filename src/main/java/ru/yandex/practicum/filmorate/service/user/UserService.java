package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.FriendsStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final ValidationUserService validationUserService = new ValidationUserService();
    private final UserStorage userStorage;
    @Autowired
    private GeneratorUserId generatorId = new GeneratorUserId();

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(@RequestBody User user) {
        log.info("Запрос на создание пользователя {} отправлен", user);
        validationUserService.validateNewUser(user);
        validationUserService.validateUniqEmail(user);
        user.setId(generatorId.generate());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя {} отправлен", user);
        validationUserService.validateNewUser(user);
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getById(long id) {
        return userStorage.getUserById(id);
    }

    public Map<User, FriendsStatus> getUserFriends(long id) {
        return userStorage.getUserFriends(id);
    }

    public void addInFriendList(long userFirstId, long userSecondId) {
        log.info("Запрос на добавление в друзья пользователей id={} и id={} направлен", userFirstId, userSecondId);
        userStorage.addInFriendList(userFirstId, userSecondId);
    }

    public void removeFriendList(long userFirstId, long userSecondId) {
        log.info("Запрос на удаление из друзей пользователей id={} и id={} направлен", userFirstId, userSecondId);
        userStorage.removeFriendList(userFirstId, userSecondId);
    }

    public List<User> showCommonFriends(Long userFirstId, Long userSecondId) {
       return userStorage.showCommonFriends(userFirstId, userSecondId);
    }
}