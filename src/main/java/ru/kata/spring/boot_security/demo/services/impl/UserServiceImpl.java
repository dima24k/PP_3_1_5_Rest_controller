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
    public Optional<User> findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Transactional
    @Override
    public void newUser(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword() );
        user.setPassword(encodePassword);

        userDao.newUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User user, Long id) {
        userDao.updateUser(user, id);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    public List<Role> getRoles() {
        return userDao.getRoles();
    }
}
