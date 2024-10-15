package org.example.filmorate.storage;

import org.example.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(int id);
    User getUserById(int id);
    List<User> getAllUsers();
    User addFriend(int userId, int friendId);
    List<User> getFriendsById(int userId);
    void deleteFriend(int userId, int friendId);
    List<User> getCommonFriends(int id, int otherId);
}
