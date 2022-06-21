package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

@Slf4j
@Service
public class ValidationUserService {

    private final UserStorage userStorage;

    @Autowired
    public ValidationUserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void validateUserId(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            log.error("Пользователь с id={} не существует", id);
            throw new NotFoundException("Пользователя с id=" + id + " не существует");
        }
    }

    public void validateUniqEmail(User user) {
        for (User u : userStorage.getUsers().values()) {
            if (user.getEmail().equals(u.getEmail())) {
                throw new ValidationException("Такой пользователь уже существует под id " + u.getId());
            }
        }
    }

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