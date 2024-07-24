package ru.kata.spring.boot_security.demo.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleDao roleDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> getRoles() {
        return userDao.getRoles();
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Transactional
    @Override
    public void newUser(User user, List<Long> roleIds) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        List<Role> roles = roleIds.stream()
                .map(roleId -> roleDao.findRoleById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + roleId)))
                .collect(Collectors.toList());
        user.setRoles(roles);

        userDao.newUser(user);
    }


    @Transactional
    @Override
    public void updateUser(User user, Long id, List<Long> roleIds) {
        List<Role> roles = roleIds.stream()
                .map(roleId -> roleDao.findRoleById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + roleId)))
                .collect(Collectors.toList());
        user.setRoles(roles);

        userDao.updateUser(user, id);
    }
}
