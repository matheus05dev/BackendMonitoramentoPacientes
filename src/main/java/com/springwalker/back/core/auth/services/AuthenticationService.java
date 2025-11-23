package com.springwalker.back.core.auth.services;

import com.springwalker.back.core.log.service.LogService;
import com.springwalker.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository repository;
    private final LogService logService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logService.logEvent("TENTATIVA_AUTENTICACAO", "Usuário tentou fazer login: " + username);
        return repository.findByUsername(username)
                .orElseThrow(() -> {
                    logService.logEvent("FALHA_AUTENTICACAO", "Usuário não encontrado: " + username);
                    return new UsernameNotFoundException("Usuário não encontrado com o nome de usuário: " + username);
                });
    }
}
