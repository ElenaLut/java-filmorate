package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorId;
import ru.yandex.practicum.filmorate.service.user.ValidationUserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final ValidationUserService validationUserService;
    private final Map<Long, User> users = new HashMap<>();
    private GeneratorId generatorId;

    @Autowired
    public InMemoryUserStorage(GeneratorId generatorId, ValidationUserService validationUserService) {
        this.generatorId = generatorId;
        this.validationUserService = validationUserService;
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User addUser(User user) {
        log.info("Запрос на создание пользователя {} отправлен", user);
        if (users.containsKey(user.getId())) {
            log.error("Пользователь {} существует", user);
            throw new IllegalStateException("Пользователь с id=" + user.getId() + " уже существует.");
        }
        validationUserService.validateNewUser(user);
        validationUserService.validateUniqEmail(user);
        user.setId(generatorId.generate());
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя {} отправлен", user);
        validationUserService.validateUserId(user.getId());
        validationUserService.validateNewUser(user);
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
}