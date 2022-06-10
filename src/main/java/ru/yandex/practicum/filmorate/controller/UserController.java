package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private GeneratorId countId = new GeneratorId();

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Запрос на создание пользователя {} отправлен", user);
        if (users.containsKey(user.getId()) || checkEmail(user)) {
            throw new ValidationException("Такой пользователь уже существует");
        }
        user.setId(countId.generate());
            newUserCheck(user);
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
            newUserCheck(user);
            users.put(user.getId(), user);
            log.info("Пользователь изменен: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Всего пользователей на портале: {}", users.size());
        return users.values();
    }

    private boolean checkEmail(User user) {
        boolean emailInBase = false;
        for (User u : users.values()) {
            if (user.getEmail().equals(u.getEmail())) {
                emailInBase = true;
            }
        }
        return emailInBase;
    }

    private void newUserCheck(User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn("Электронная почта не может быть пустой и должна содержать символ @, текущая: {}", user.getEmail());
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы, текущий: {}", user.getLogin());
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем: {}", user.getBirthday());
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
