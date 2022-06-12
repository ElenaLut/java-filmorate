package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private GeneratorId generatorId = new GeneratorId();

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Запрос на создание пользователя {} отправлен", user);
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Такой пользователь уже существует");
        }
        if (containsEmail(user) != null) {
            User oldUser = containsEmail(user);
            throw new ValidationException("Такой пользователь уже существует под id " + oldUser.getId());
        }
        user.setId(generatorId.generate());
        validateNewUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }


    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Запрос на обновление пользователя {} отправлен", user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Такого пользователя не существует");
        }
        validateNewUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь изменен: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Всего пользователей на портале: {}", users.size());
        return users.values();
    }

    private User containsEmail(User user) {
        for (User u : users.values()) {
            if (user.getEmail().equals(u.getEmail())) {
                return u;
            }
        }
        return null;
    }

    private void validateNewUser(User user) {
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