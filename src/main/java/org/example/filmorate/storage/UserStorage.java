package org.example.filmorate.storage;

import org.example.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(int id);
    User getUserById(int id);
    Collection<User> getAllUsers();
}
