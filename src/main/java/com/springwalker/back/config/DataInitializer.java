package com.springwalker.back.config;

import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByLogin("admin.math@mail.com").isEmpty()) {
            User adminUser = new User(
                    "admin.math@mail.com",
                    passwordEncoder.encode("admin"),
                    "ROLE_ADMIN" // Adicionando a role de admin
            );
            userRepository.save(adminUser);
        }
    }
}
