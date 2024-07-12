package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public String admin(Model model, Principal principal) {
        User user = userRepository.findByUserName(principal.getName() ).get();
        model.addAttribute("admin", user);
        return "admin";
    }

    @GetMapping("/all")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAll() );
        return "all";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User() );
        return "new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user) {
        userService.newUser(user);
        return "redirect:/admin/all";
    }

    @GetMapping("/update")
    public String updateUserGet(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id) );
        return "update";
    }

    @PostMapping("/update")
    public String updateUserPost(@RequestParam(value="id") Long id, @ModelAttribute User user) {
        userService.updateUser(user, id);
        return "redirect:/admin/all";
    }
}
