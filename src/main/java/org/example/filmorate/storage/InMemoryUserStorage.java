package org.example.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.example.filmorate.exception.EntityNotFoundException;
import org.example.filmorate.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int uniqueId = 1;

    @Override
    public User createUser(User user) {
        user.setId(uniqueId++);
        users.put(user.getId(), user);

        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновлен пользователь: {}", user);
            return user;
        } else {
            log.warn("Пользователь с ID " + user.getId() + " не найден");
            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
        }
    }

    @Override
    public void deleteUser(int id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            log.warn("Пользователь с ID " + id + " не найден");
            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
        }
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.warn("Пользователь с ID " + id + " не найден");
            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addFriend(int userId, int friendId) {
        return null;
    }

    @Override
    public List<User> getFriendsById(int userId) {
        return null;
    }

    @Override
    public void deleteFriend(int userId, int friendId) {

    }
}
