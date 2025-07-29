package com.myrestaurant.user.controller;

import com.myrestaurant.user.dto.UserDTO;
import com.myrestaurant.user.dto.UserCreateDTO;
import com.myrestaurant.user.dto.UserRegistrationDTO;
import com.myrestaurant.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        return userService.createUser(createDTO);
    }

    @PostMapping("/register")
    public UserDTO registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        return userService.registerUser(registrationDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
