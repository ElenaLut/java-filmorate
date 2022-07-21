package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    private Map<User, FriendsStatus> friends = new HashMap<>();

    public void addFriend(User friend, FriendsStatus status) {
        friends.put(friend, status);
    }
}