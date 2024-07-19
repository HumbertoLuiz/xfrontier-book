package com.nofrontier.book.unittests.mockito.services;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.nofrontier.book.auth.services.AuthService;
import com.nofrontier.book.domain.exceptions.UserNotFoundException;
import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.domain.repository.UserRepository;

@SpringBootTest
class AuthServiceUnitTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsername_Success() {
        // Simulate finding user in the database
        User mockUser = new User();
        mockUser.setEmail("admin@mail.com");
        mockUser.setPassword("$2a$10$3bHtw88LCzLnEB0zbBr.Uu2dwH2qE4IsBEw1S0SH2JZMSh2idTNTa"); // Replace with actual hashed password

        when(userRepository.findByEmail("admin@mail.com")).thenReturn(Optional.of(mockUser));

        // Test loading user by username (email)
        var userDetails = authService.loadUserByUsername("admin@mail.com");

        assertEquals("admin@mail.com", userDetails.getUsername());
        // Add more assertions based on your UserDetailsService implementation
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Simulate user not found in the database
        when(userRepository.findByEmail("admin@mail.com")).thenReturn(Optional.empty());

        // Test should throw UsernameNotFoundException
        assertThrows(UserNotFoundException.class, () -> authService.loadUserByUsername("admin@mail.com"));
    }
}
