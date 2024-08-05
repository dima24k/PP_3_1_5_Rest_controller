package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUserName(String username);

    User findById(Long id);

    List<User> getAll();

    void newUser(User user);

    void updateUser(User user,Long id);

    void deleteUser(Long id);
}
