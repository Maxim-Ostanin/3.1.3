package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Controller
public class AdminController {

    private final UserRepository userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserRepository userService,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String showAllUsers(ModelMap model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);
        return "admin";
    }

    @DeleteMapping("/admin/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/new")
    public String addNewUser(@ModelAttribute("user") User user, Model model) {
        List<Role> roles = roleRepository.getAllRoles();
        model.addAttribute("roles", roles);
        return "new";
    }

    @PostMapping("admin")
    public String createUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/{id}/edit")
    public String editUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        List<Role> roles = roleRepository.getAllRoles();
        model.addAttribute("roles", roles);
        return "edit";
    }

    @PatchMapping("admin/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        if (user.getPassword().equals(userService.getUserById(id).getPassword())) {
            user.setPassword(user.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.updateUser(id, user);
        return "redirect:/admin";
    }
}
