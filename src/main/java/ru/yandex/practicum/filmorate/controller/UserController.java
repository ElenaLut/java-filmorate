package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addInFriendList(@PathVariable("id") long id, @PathVariable long friendId) {
        userService.addInFriendList(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriendList(@PathVariable("id") long id, @PathVariable long friendId) {
        userService.removeFriendList(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> showCommonFriends(@PathVariable("id") long id, @PathVariable long otherId) {
        return userService.showCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") long id) {
        return userService.getUserFriends(id);
    }
}