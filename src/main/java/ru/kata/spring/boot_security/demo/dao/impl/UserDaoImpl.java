package ru.kata.spring.boot_security.demo.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findByUserName(String username) {
        User user = null;
        try {
            user = (User) entityManager.createQuery("select u from User u left join fetch u.roles where u.userName =:username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            // handle exception
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("select u from User u left join fetch u.roles", User.class).getResultList();
    }

    @Override
    public void newUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user, Long id) {
        Query query = entityManager.createQuery("UPDATE User u SET u.userName = :name, u.age = :age, u.email = :email WHERE u.id = :id");

        query.setParameter("name", user.getUserName() );
        query.setParameter("age", user.getAge() );
        query.setParameter("email", user.getEmail() );
        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public void deleteUser(Long id) {
        Query query = entityManager.createQuery("DELETE FROM User u WHERE u.id = :userId");
        query.setParameter("userId", id);
        query.executeUpdate();
    }
}
