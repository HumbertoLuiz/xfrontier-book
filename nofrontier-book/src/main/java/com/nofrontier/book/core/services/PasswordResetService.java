package com.nofrontier.book.core.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nofrontier.book.domain.exceptions.PasswordResetNotFound;
import com.nofrontier.book.domain.model.PasswordReset;
import com.nofrontier.book.domain.repository.PasswordResetRepository;
import com.nofrontier.book.domain.repository.UserRepository;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordReset createPasswordReset(String email) {
        if (userRepository.existsByEmail(email)) {
            var passwordReset = PasswordReset.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .build();
            return repository.save(passwordReset);
        }
        return null;
    }

    public void resetPassword(String passwordResetToken, String newPassword) {
        var passwordReset = findPasswordResetPorToken(passwordResetToken);
        var user = userRepository.findByEmail(passwordReset.getEmail()).get();
        var newPasswordHash = passwordEncoder.encode(newPassword);
        user.setPassword(newPasswordHash);
        userRepository.save(user);
        repository.delete(passwordReset);
    }

    private PasswordReset findPasswordResetPorToken(String passwordResetToken) {
        return repository.findByToken(passwordResetToken)
            .orElseThrow(PasswordResetNotFound::new);
    }

}
