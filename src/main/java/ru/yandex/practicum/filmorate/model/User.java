package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
//@Builder
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    @JsonIgnore
    private Map<User, FriendsStatus> friends = new HashMap<>();

    /*public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    */

    public void addFriend(User friend, FriendsStatus status) {
        friends.put(friend, status);
    }
}