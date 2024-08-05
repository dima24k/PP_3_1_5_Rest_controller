package ru.kata.spring.boot_security.demo.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Role> findRoleById(long id) {
        Role role = null;
        try {
            role = (Role) entityManager.createQuery("SELECT r FROM Role r WHERE r.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            // handle exception
        }
        return Optional.ofNullable(role);
    }

    @Override
    public List<Role> getRoles() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }
}
