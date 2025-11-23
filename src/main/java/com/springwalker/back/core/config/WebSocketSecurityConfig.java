package com.springwalker.back.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.lang.NonNull;
import java.util.Collections;
import java.util.function.Supplier;

@Configuration
public class WebSocketSecurityConfig {

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager() {
        MessageMatcherDelegatingAuthorizationManager.Builder messages =
                new MessageMatcherDelegatingAuthorizationManager.Builder();

        messages
                .simpTypeMatchers(SimpMessageType.CONNECT).permitAll()
                .simpDestMatchers("/app/leituras/por-atendimento").hasAnyRole("ADMIN", "MEDICO", "ENFERMEIRO")
                .simpDestMatchers("/app/notificacoes/historico").hasAnyRole("ADMIN", "MEDICO", "ENFERMEIRO")
                .anyMessage().authenticated();

        return messages.build();
    }

    @Bean
    public ChannelInterceptor authorizationChannelInterceptor(AuthorizationManager<Message<?>> messageAuthorizationManager) {
        return new ChannelInterceptor() {
            @Override
            public @NonNull Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null) {
                    // Para mensagens CONNECT, se nenhum principal estiver definido, define um anônimo
                    // Isso é para permitir que o permitAll() para CONNECT funcione corretamente
                    if (StompCommand.CONNECT.equals(accessor.getCommand()) && accessor.getUser() == null) {
                        Authentication anonymous = new AnonymousAuthenticationToken(
                                "anonymous", "anonymousUser",
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
                        accessor.setUser(anonymous);
                    }

                    // Obtém a autenticação do accessor e a envolve em um Supplier
                    // Explicitamente definindo o tipo do Supplier para evitar ambiguidades
                    Supplier<Authentication> authenticationSupplier = new Supplier<Authentication>() {
                        @Override
                        public Authentication get() {
                            return (Authentication) accessor.getUser();
                        }
                    };
                    // Usando o metodo verify não depreciado para Message<?>
                    messageAuthorizationManager.verify(authenticationSupplier, message);
                }
                return message;
            }
        };
    }
}
