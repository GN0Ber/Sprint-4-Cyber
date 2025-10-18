package com.wise.buddy.wiseBuddy.web;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    Map<String,Object> body = new LinkedHashMap<>();
    body.put("error", "validation");
    body.put("fields", ex.getBindingResult().getFieldErrors().stream().map(f -> Map.of(
        "field", f.getField(),
        "message", f.getDefaultMessage()
    )));
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneric(Exception ex) {
    // Logue internamente (logger), não exponha stack para o cliente
    Map<String,Object> body = Map.of("error","internal", "message","An unexpected error occurred");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}
