package org.example.filmorate.service;

import jakarta.validation.ValidationException;
import org.example.filmorate.exception.EntityNotFoundException;
import org.example.filmorate.model.User;
import org.example.filmorate.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
//        if (!userStorage.getAllUsers().stream().anyMatch(u -> u.getId() == user.getId())) {
//            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
//        }
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
//        User user = userStorage.getUserById(userId);
//        User friend = userStorage.getUserById(friendId);
//
//        user.getFriends().add(friendId);
//        friend.getFriends().add(userId);
//        userStorage.updateUser(user);
//        userStorage.updateUser(friend);
        userStorage.addFriend(userId, friendId);
    }

    public List<User> getAllFriendsById(int id) {
//        return userStorage.getUserById(id).getFriends().stream()
//                .map(friendId -> userStorage.getUserById(friendId))
//                .collect(Collectors.toList());
        userStorage.getUserById(id);
        return userStorage.getFriendsById(id);
    }

    public void deleteFriend(int userId, int friendId) {
//        User user = userStorage.getUserById(userId);
//        User friend = userStorage.getUserById(friendId);
//
//        user.getFriends().remove(friendId);
//        friend.getFriends().remove(userId);
//        userStorage.updateUser(user);
//        userStorage.updateUser(friend);
        userStorage.deleteFriend(userId, friendId);
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
