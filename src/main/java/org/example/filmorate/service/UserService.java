package org.example.filmorate.service;

import org.example.filmorate.model.User;
import org.example.filmorate.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addUser(User user) {
        userStorage.createUser(user);
    }

    public void updateUser(User user) {
        userStorage.getUserById(user.getId());
        userStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public List<User> getAllFriendsById(int id) {
        userStorage.getUserById(id);
        return userStorage.getFriendsById(id);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getCommonFriends(int firstUserId, int secondUserId) {
        return userStorage.getCommonFriends(firstUserId, secondUserId);
    }
}
