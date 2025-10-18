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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

  private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

  private static final List<String> SKIP_PREFIXES = List.of(
      "/actuator/health", // readiness/liveness
      "/v3/api-docs",     // OpenAPI JSON usado pelo ZAP
      "/swagger-ui"       // UI
  );

  private boolean isInfraPath(String path) {
    if (path == null) return false;
    for (String p : SKIP_PREFIXES) {
      if (path.startsWith(p)) return true;
    }
    return false;
  }

  private Bucket resolveBucket(String key) {
    // 100 req/min — API nova do Bucket4j
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

    String path = request.getRequestURI();

    // Não rate-limita endpoints de infraestrutura
    if (isInfraPath(path)) {
      chain.doFilter(req, res);
      return;
    }

    String key = getClientIp(request);
    if (resolveBucket(key).tryConsume(1)) {
      chain.doFilter(req, res);
    } else {
      response.setStatus(429);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\":\"too_many_requests\"}");
    }
  }

  private String getClientIp(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader != null && !xfHeader.isEmpty()) {
      return xfHeader.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}