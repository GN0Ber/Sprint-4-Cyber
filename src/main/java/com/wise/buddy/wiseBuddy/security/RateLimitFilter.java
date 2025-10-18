package com.wise.buddy.wiseBuddy.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

  private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

  private Bucket resolveBucket(String key) {
    Bandwidth limit = Bandwidth.builder()
        .capacity(100)
        .refillIntervally(100, Duration.ofMinutes(1))
        .build();

    return buckets.computeIfAbsent(key, k -> Bucket.builder().addLimit(limit).build());
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    String key = request.getRemoteAddr(); // pode trocar por sub do JWT depois

    if (resolveBucket(key).tryConsume(1)) {
      chain.doFilter(req, res);
    } else {
      response.setStatus(429);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\":\"too_many_requests\"}");
    }
  }
}

