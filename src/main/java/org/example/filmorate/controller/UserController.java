package org.example.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    List<User> users = new ArrayList<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.add(user);
        log.info("Создан новый пользователь: {}", user);
        return user;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @Valid @RequestBody User updatedUser) {
        for (User user : users) {
            if (user.getId() == id) {
                user.setEmail(updatedUser.getEmail());
                user.setLogin(updatedUser.getLogin());
                user.setName(updatedUser.getName());
                user.setBirthday(updatedUser.getBirthday());
                log.info("Пользователь с ID {} обновлен: {}", id, updatedUser);
                return user;
            }
        }
        String errorMsg = "Пользователь с ID " + id + " не найден.";
        log.error(errorMsg);
        return null;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}
