package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class AdminServiceImpl implements AdminService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
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
    public void newUser(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword() );
        user.setPassword(encodePassword);

        Optional<Role> role = userDao.findRoleByName("ROLE_USER");

        if (role.isPresent() ) {
            user.getRoles().add(role.get() );
        } else {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            user.getRoles().add(newRole);
        }

        userDao.newUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User user, Long id) {
        userDao.updateUser(user, id);
    }
}
