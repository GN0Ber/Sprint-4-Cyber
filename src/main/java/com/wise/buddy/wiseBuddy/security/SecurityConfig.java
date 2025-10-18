package com.wise.buddy.wiseBuddy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Actuator health/liveness/readiness liberados
                .requestMatchers("/actuator/health/**").permitAll()
                // Swagger/OpenAPI liberados
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                // o resto exige auth
                .anyRequest().authenticated()
            )
            // Você pode usar formLogin() ou httpBasic() só para dev/CI
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
