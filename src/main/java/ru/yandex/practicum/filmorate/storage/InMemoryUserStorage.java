package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private GeneratorId generatorId = new GeneratorId();

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User addUser(User user) {
        log.info("Запрос на создание пользователя {} отправлен", user);
        if (users.containsKey(user.getId())) {
            log.error("Пользователь {} существует", user);
            throw new ValidationException("Такой пользователь уже существует");
        }
        validateUniqEmail(user);
        user.setId(generatorId.generate());
        validateNewUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public void validateUserId(long id) {
        if (!users.containsKey(id)) {
            log.error("Пользователь с id={} не существует", id);
            throw new ObjectNotFoundException("Пользователя с id=" + id + " не существует");
        }
    }

    @Override
    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя {} отправлен", user);
        validateUserId(user.getId());
        validateNewUser(user);
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
    public void validateUniqEmail(User user) {
        for (User u : users.values()) {
            if (user.getEmail().equals(u.getEmail())) {
                throw new ValidationException("Такой пользователь уже существует под id " + u.getId());
            }
        }
    }

    @Override
    public void validateNewUser(User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @, текущая: {}", user.getEmail());
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы, текущий: {}", user.getLogin());
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем: {}", user.getBirthday());
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}