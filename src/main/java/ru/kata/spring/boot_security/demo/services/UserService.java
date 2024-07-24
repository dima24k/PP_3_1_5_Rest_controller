package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<Role> getRoles();
    Optional<User> findByUserName(String username);

    User getUserById(Long id);

    List<User> getAll();

    void newUser(User user, List<Long> roleIds);

    void updateUser(User user, Long id, List<Long> roleIds);

    void deleteUser(Long id);
}
