package com.springwalker.back.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Configuration
    public static class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

        @Override
        protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
            messages
                    .simpDestMatchers("/app/leituras/por-atendimento").hasAnyRole("ADMIN", "MEDICO", "ENFERMEIRO")
                    .simpDestMatchers("/app/notificacoes/historico").hasAnyRole("ADMIN", "MEDICO", "ENFERMEIRO")
                    .anyMessage().authenticated();
        }

        @Override
        protected boolean sameOriginDisabled() {
            return true;
        }
    }
}
