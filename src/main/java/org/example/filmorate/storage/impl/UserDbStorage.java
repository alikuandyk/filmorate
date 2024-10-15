package org.example.filmorate.storage.impl;

import org.example.filmorate.exception.EntityNotFoundException;
import org.example.filmorate.model.User;
import org.example.filmorate.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> map = Map.of(
                "login", user.getLogin(),
                "email", user.getEmail(),
                "name", user.getName(),
                "birthday", user.getBirthday()
        );

        int id = insert.executeAndReturnKey(map).intValue();
        user.setId(id);

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET login = ?, email = ?, name = ?, birthday = ? WHERE users.id = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getEmail(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE users.id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE users.id = ?";

        return jdbcTemplate.query(sql, this::rowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("select * from users", this::rowMapper);
    }

    @Override
    public User addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        String sql = "insert into friendships (user1_id, user2_id) values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        return user;
    }

    @Override
    public List<User> getFriendsById(int userId) {
        String sql = """
                SELECT u.id, u.email, u.login, u.name, u.birthday
                FROM friendships f
                JOIN users u ON u.id = f.user2_id
                WHERE f.user1_id = ?
                """;

        return jdbcTemplate.query(sql, this::rowMapper, userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        String sql = """
                delete from friendships where user1_id = ? and user2_id = ?
                """;

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sql = """
                SELECT u.id, u.email, u.login, u.name, u.birthday
                FROM users u
                JOIN friendships f1 ON u.id = f1.user2_id
                JOIN friendships f2 ON u.id = f2.user2_id
                WHERE f1.user1_id = ? AND f2.user1_id = ?""";

        return jdbcTemplate.query(sql, this::rowMapper, id, otherId);
    }

    public User rowMapper (ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        );
    }
}
