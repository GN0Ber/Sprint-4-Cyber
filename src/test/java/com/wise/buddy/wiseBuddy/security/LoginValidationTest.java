package com.wise.buddy.wiseBuddy.security;

import org.junit.jupiter.api.Test; // JUnit 5
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginValidationTest {

    @Autowired
    MockMvc mvc;

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        var body = "{\"email\":\"not-an-email\",\"password\":\"12345678\"}";
        mvc.perform(post("/wise-buddy/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectShortPassword() throws Exception {
        var body = "{\"email\":\"user@mail.com\",\"password\":\"123\"}";
        mvc.perform(post("/wise-buddy/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isBadRequest());
    }
}
