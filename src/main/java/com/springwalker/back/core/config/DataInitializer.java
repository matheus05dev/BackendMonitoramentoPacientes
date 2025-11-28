package com.springwalker.back.core.config;

import com.springwalker.back.user.role.Role;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User(
                    "admin",
                    passwordEncoder.encode("admin"),
                    Role.ADMIN // Adicionando a role de admin
            );
            userRepository.save(adminUser);
        }
    }
}
