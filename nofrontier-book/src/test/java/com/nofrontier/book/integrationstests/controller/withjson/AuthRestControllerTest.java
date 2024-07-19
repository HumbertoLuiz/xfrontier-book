package com.nofrontier.book.integrationstests.controller.withjson;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nofrontier.book.core.enums.UserType;
import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.domain.repository.UserRepository;
import com.nofrontier.book.dto.v1.requests.TokenRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAuthenticate() throws Exception {
        // Configuração de dados de teste
        User user = new User();
        user.setCompleteName("Test User");
        user.setEmail("testuser@mail.com");
        user.setPassword(passwordEncoder.encode("password@123"));
        user.setEnabled(true);
        user.setUserType(UserType.ADMIN);
        userRepository.save(user);

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setEmail("testuser@mail.com");
        tokenRequest.setPassword("password@123");

        mockMvc.perform(post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequest)))
                .andExpect(status().isOk());
    }
}
