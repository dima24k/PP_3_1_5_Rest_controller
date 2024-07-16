package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Role> findRoleByName(String name) {
        Role role = null;
        try {
            role = (Role) entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name")
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            // handle exception
        }
        return Optional.ofNullable(role);
    }
}
