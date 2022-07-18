package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.FriendsStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    User addUser(@RequestBody User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    User getUserById(long id);

    void addInFriendList(long userFirstId, long userSecondId);

    void removeFriendList(long userFirstId, long userSecondId);

    List<User> showCommonFriends(Long userFirstId, Long userSecondId);

    Map<User, FriendsStatus> getUserFriends(long id);
}