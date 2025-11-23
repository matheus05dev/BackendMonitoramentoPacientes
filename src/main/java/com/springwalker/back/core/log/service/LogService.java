package com.springwalker.back.core.log.service;

import com.springwalker.back.core.log.model.Log;
import com.springwalker.back.core.log.repository.LogRepository;
import com.springwalker.back.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class LogService {


    private final LogRepository logRepository;

    public void logEvent(String eventType, String description) {
        Long userId = getCurrentUserId(); // Recupera o ID do usuário logado

        Log log = new Log(eventType, userId, description); // Usa o novo construtor
        logRepository.save(log);
    }

    // Metodo auxiliar para obter o ID do usuário logado
    private Long getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof User) // Verificar se o principal é uma instância de User
                .map(principal -> ((User) principal).getId()) // Fazer o cast para User e obter o ID
                .orElse(null);
    }
}
