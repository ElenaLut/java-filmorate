package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorId;
import ru.yandex.practicum.filmorate.service.user.ValidationUserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final ValidationUserService validationUserService = new ValidationUserService();
    private final Map<Long, User> users = new HashMap<>();
    private GeneratorId generatorId;

    @Autowired
    public InMemoryUserStorage(GeneratorId generatorId) {
        this.generatorId = generatorId;
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
        validateUniqEmail(user);
        user.setId(generatorId.generate());
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя {} отправлен", user);
        validateUserId(user.getId());
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
    public void validateUserId(long id) {
        if (users.containsKey(id)) {
            log.error("Пользователь с id={} не существует", id);
            throw new NotFoundException("Пользователя с id=" + id + " не существует");
        }
    }

    @Override
    public void validateUniqEmail(User user) {
        for (User u : users.values()) {
            if (user.getEmail().equals(u.getEmail())) {
                throw new ValidationException("Такой пользователь уже существует под id " + u.getId());
            }
        }
    }

    @Override
    public User getUserById(long id) {
        validateUserId(id);
        return users.get(id);
    }
}