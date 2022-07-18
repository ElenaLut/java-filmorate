package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendsStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.GeneratorUserId;
import ru.yandex.practicum.filmorate.service.user.ValidationUserService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final ValidationUserService validationUserService = new ValidationUserService();
    private final Map<Long, User> users = new HashMap<>();

    @Autowired
    private GeneratorUserId generatorId = new GeneratorUserId();

    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User addUser(User user) {
        if (users.containsKey(user.getId())) {
            log.error("Пользователь {} существует", user);
            throw new IllegalStateException("Пользователь с id=" + user.getId() + " уже существует.");
        }
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validationUserService.validateUserId(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь изменен: {}", user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Всего пользователей на портале: {}", users.size());
        return users.values();
    }

    @Override
    public User getUserById(long id) {
        validationUserService.validateUserId(id);
        return users.get(id);
    }

    @Override
    public void addInFriendList(long userFirstId, long userSecondId) {
        User userFirst = getUserById(userFirstId);
        User userSecond = getUserById(userSecondId);
        validationUserService.validateUserId(userFirstId);
        validationUserService.validateUserId(userSecondId);
        userFirst.addFriend(userSecond, FriendsStatus.CONFIRMED);
        if (userSecond.getFriends().containsKey(userFirst)) {
            userSecond.addFriend(userFirst, FriendsStatus.CONFIRMED);
        } else {
            userSecond.addFriend(userFirst, FriendsStatus.UNCONFIRMED);
        }
        updateUser(userFirst);
        updateUser(userSecond);
        log.info("Пользователи id={} и id={} добавлены в друзья", userFirstId, userSecondId);
    }

    @Override
    public void removeFriendList(long userFirstId, long userSecondId) {
        User userFirst = getUserById(userFirstId);
        User userSecond = getUserById(userSecondId);
        validationUserService.validateUserId(userFirstId);
        validationUserService.validateUserId(userSecondId);
        userFirst.getFriends().remove(userSecondId);
        userSecond.getFriends().remove(userFirstId);
       updateUser(userFirst);
        updateUser(userSecond);
        log.info("Пользователи id={} и id={} удалены из друзей", userFirstId, userSecondId);
    }

    @Override
    public List<User> showCommonFriends(Long userFirstId, Long userSecondId) {
        Set<User> userFirstFriends = getUserFriends(userFirstId).keySet();
        Set<User> userSecondFriends = getUserFriends(userSecondId).keySet();
        return userFirstFriends
                .stream()
                .filter(userSecondFriends::contains)
                .collect(Collectors.toList());
    }

    @Override
    public Map<User, FriendsStatus> getUserFriends(long id) {
        User user = getUserById(id);
        return user.getFriends();
    }
}