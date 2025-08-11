package com.myrestaurant.booking;

import com.myrestaurant.user.model.User;
import com.myrestaurant.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
public class PasswordDebugTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void debugPasswordIssue() {
        // 1. Verifica se l'utente admin esiste
        Optional<User> adminUser = userRepository.findByUsername("admin");

        if (adminUser.isPresent()) {
            User user = adminUser.get();
            System.out.println("=== UTENTE ADMIN TROVATO ===");
            System.out.println("ID: " + user.getId());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password Hash nel DB: " + user.getPassword());
            System.out.println("Ruoli: " + user.getRoles());

            // 2. Testa se la password "admin123" matcha l'hash nel database
            String rawPassword = "admin123";
            boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
            System.out.println("Password 'admin123' matches hash: " + matches);

            // 3. Genera un nuovo hash per admin123
            String newHash = passwordEncoder.encode(rawPassword);
            System.out.println("Nuovo hash per 'admin123': " + newHash);

            // 4. Verifica che il nuovo hash funzioni
            boolean newMatches = passwordEncoder.matches(rawPassword, newHash);
            System.out.println("Nuovo hash matches 'admin123': " + newMatches);

            // 5. Aggiorna l'utente con il nuovo hash
            user.setPassword(newHash);
            userRepository.save(user);
            System.out.println("Password aggiornata nel database!");

        } else {
            System.out.println("=== UTENTE ADMIN NON TROVATO ===");

            // Crea l'utente admin
            User newAdmin = new User();
            newAdmin.setUsername("admin");
            newAdmin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(newAdmin);
            System.out.println("Utente admin creato!");
        }

        // 6. Lista tutti gli utenti
        System.out.println("\n=== TUTTI GLI UTENTI ===");
        userRepository.findAll().forEach(user -> {
            System.out.println("Username: " + user.getUsername() + ", Hash: " + user.getPassword());
        });
    }
}