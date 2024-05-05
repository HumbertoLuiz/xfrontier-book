package com.nofrontier.book.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nofrontier.book.domain.exceptions.UserNotFoundException;
import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.domain.repository.UserRepository;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getEmailLoggedUser() {
        return getAuthentication().getName();
    }

    public User getLoggedUser() {
        var email= getEmailLoggedUser();
        var message= String.format("User with email %s not found", email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(message));

    }
}
