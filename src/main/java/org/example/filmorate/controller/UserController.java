package org.example.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    Map<Integer, User> users = new HashMap<>();

    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Создан новый пользователь: {}", user);
        return user;
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable int id, @Valid @RequestBody User updatedUser) {
        validateUser(updatedUser);
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (entry.getValue().getId() == id) {
                entry.getValue().setEmail(updatedUser.getEmail());
                entry.getValue().setLogin(updatedUser.getLogin());
                entry.getValue().setName(updatedUser.getName());
                entry.getValue().setBirthday(updatedUser.getBirthday());
                log.info("Пользователь с ID {} обновлен: {}", id, updatedUser);
                return entry.getValue();
            }
        }
        String errorMsg = "Пользователь с ID " + id + " не найден.";
        log.warn(errorMsg);
        throw new ValidationException("Пользователь с указанным ID не найден.");
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        ArrayList<User> userArrayList = new ArrayList<>();

        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            userArrayList.add(entry.getValue());
        }

        return userArrayList;
    }

    private void validateUser(User user) {
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now().atStartOfDay())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
