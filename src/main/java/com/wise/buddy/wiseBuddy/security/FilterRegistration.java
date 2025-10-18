package com.wise.buddy.wiseBuddy.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRegistration {

  @Bean
  public FilterRegistrationBean<RateLimitFilter> rateLimitRegistration(RateLimitFilter filter) {
    var reg = new FilterRegistrationBean<>(filter);
    reg.setOrder(1); // executa cedo
    return reg;
  }
}
