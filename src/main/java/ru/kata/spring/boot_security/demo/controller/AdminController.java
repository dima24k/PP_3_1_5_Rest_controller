package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;
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
        model.addAttribute("roleList", userService.getRoles());
        model.addAttribute("users", userService.getAll() );
        return "admin";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("roleIds") List<Long> roleIds) {
        userService.newUser(user, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") Long id,
                         @RequestParam("roleIds") List<Long> roleIds) {
        userService.updateUser(user, id, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
