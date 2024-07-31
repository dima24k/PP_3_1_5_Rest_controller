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
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public ResponseEntity<User> getUser(Principal principal) {
        return ResponseEntity.ok(userService.findByUserName(principal.getName() )
                .orElseThrow( () -> new NoSuchElementException("User not found") ) );
    }

    @GetMapping
    public ResponseEntity<List<User> > getUsers() {
        return ResponseEntity.ok(userService.getAll() );
    }

    @PostMapping("/add")
    public void createUser(@RequestBody User user) {
       userService.newUser(user);
    }

    @PutMapping("/update/{id}")
    public void update(@RequestBody User user, @PathVariable Long id) {
        userService.updateUser(user, id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
