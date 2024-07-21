package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUser(Principal principal, Model model) {
        User user = userService.findByUserName(principal.getName() )
                .orElseThrow( () -> new NoSuchElementException("User not found") );

        model.addAttribute("admin", user);
        model.addAttribute("roles", user.getRolesName() );
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
        return "admin";
    }

    @PostMapping("/update")
    public String updateUserPost(@RequestParam(value="id") Long id, @ModelAttribute User user) {
        userService.updateUser(user, id);
        return "redirect:/admin/all";
    }

    @GetMapping("/delete")
    public String deleteUserGet(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("id", id);
        return "delete";
    }

    @PostMapping("/delete")
    public String deleteUserPost(@RequestParam(value="id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/all";
    }
}
