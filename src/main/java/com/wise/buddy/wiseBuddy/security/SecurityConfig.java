package com.wise.buddy.wiseBuddy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      // Para API com token/mobile. Se você usa sessão/form, habilite CSRF.
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
          .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
          .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
          .requestMatchers(HttpMethod.POST,
              "/wise-buddy/v1/users/login",
              "/wise-buddy/v1/users/register").permitAll()
          .anyRequest().authenticated()
      )
      .httpBasic(Customizer.withDefaults()) // troque por JWT/OAuth2 se já tiver
      .headers(headers -> headers
          .httpStrictTransportSecurity(hsts -> hsts
              .includeSubDomains(true).preload(true).maxAgeInSeconds(31536000))
          .contentSecurityPolicy(csp -> csp
              .policyDirectives("default-src 'none'; frame-ancestors 'none'; base-uri 'none'"))
          .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
          .frameOptions(frame -> frame.deny())
          .referrerPolicy(ref -> ref.policy(
              org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
          .addHeaderWriter(new StaticHeadersWriter("Permissions-Policy",
              "geolocation=(), microphone=(), camera=()"))
      );

    return http.build();
  }
}
