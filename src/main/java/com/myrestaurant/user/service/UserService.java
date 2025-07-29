package com.myrestaurant.user.service;

import com.myrestaurant.exception.InvalidBookingException;
import com.myrestaurant.user.dto.UserDTO;
import com.myrestaurant.user.dto.UserCreateDTO;
import com.myrestaurant.user.dto.UserRegistrationDTO;
import com.myrestaurant.user.model.Role;
import com.myrestaurant.user.model.User;
import com.myrestaurant.user.repository.RoleRepository;
import com.myrestaurant.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserCreateDTO createDTO) {
        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Ruolo ROLE_USER non trovato!"));

        user.setRoles(Set.of(defaultRole));

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }

    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (!registrationDTO.isPasswordMatching()) {
            throw new InvalidBookingException("Le password non coincidono");
        }

        // Verifica che l'username non esista già
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new InvalidBookingException("Username già esistente");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Ruolo ROLE_USER non trovato!"));

        user.setRoles(Set.of(defaultRole));

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
}
