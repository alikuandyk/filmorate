package org.example.filmorate.service;

import org.example.filmorate.model.User;
import org.example.filmorate.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllFriendsById() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public List<User> getCommonFriends(int firstUserId, int secondUserId) {
        Set<Integer> firstUserFriends = new HashSet<>(userStorage.getUserById(firstUserId).getFriends());
        Set<Integer> secondUserFriends = new HashSet<>(userStorage.getUserById(secondUserId).getFriends());
        firstUserFriends.retainAll(secondUserFriends);

        return firstUserFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
