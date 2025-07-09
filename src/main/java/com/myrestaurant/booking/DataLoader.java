package com.myrestaurant.booking;

import com.myrestaurant.booking.user.model.Role;
import com.myrestaurant.booking.user.model.User;
import com.myrestaurant.booking.user.repository.RoleRepository;
import com.myrestaurant.booking.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.findByName("ROLE_USER") == null) {
                roleRepository.save(new Role("ROLE_USER"));
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                Role userRole = roleRepository.findByName("ROLE_USER");

                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRoles(Set.of(userRole));

                userRepository.save(user);
            }
        };
    }
}
