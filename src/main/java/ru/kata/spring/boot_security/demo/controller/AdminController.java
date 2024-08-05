package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/current")
    public ResponseEntity<User> getUser(Principal principal) {
        return ResponseEntity.ok(userService.findByUserName(principal.getName() )
                .orElseThrow( () -> new NoSuchElementException("User not found") ) );
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> showUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id) );
    }

    @GetMapping
    public ResponseEntity<List<User> > getUsers() {
        return ResponseEntity.ok(userService.getAll() );
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role> > roles() {
        return ResponseEntity.ok(roleService.getRoles() );
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
       userService.newUser(user);
       return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        userService.updateUser(user, user.getId() );
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
