package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            System.out.println("Такой пользователь уже есть");
        } else {
            newUserCheck(user);
            users.put(user.getId(), user);
            log.info("{}", user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            newUserCheck(user);
            users.put(user.getId(), user);
            log.info("{}", user);
        } else {
            System.out.println("Такого пользователя не существует");
        }
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Всего пользователей на портале: {}", users.size());
        return users.values();
    }

    private void newUserCheck(User user) throws ValidationException {
        if (user.getEmail().isBlank() || user.getEmail().equals(null) || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().equals(null) || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isBlank() || user.getName().equals(null)) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
