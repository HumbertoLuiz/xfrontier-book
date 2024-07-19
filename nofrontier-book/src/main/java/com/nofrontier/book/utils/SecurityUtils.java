package com.nofrontier.book.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nofrontier.book.core.enums.UserType;
import com.nofrontier.book.domain.exceptions.UserNotFoundException;
import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.domain.repository.UserRepository;

@Component
public class SecurityUtils {

    @Autowired
    @Lazy
    private UserRepository userRepository;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Boolean isCustomer() {
        var authentication = getAuthentication();
        var userType = UserType.CUSTOMER.name();
        return authentication.getAuthorities()
            .stream()
            .anyMatch(authority -> authority.getAuthority().equals(userType));
    }

    public String getEmailUserLogged() {
        return getAuthentication().getName();
    }

    public User getLoggedUser() {
        var email = getEmailUserLogged();
        var mensagem = String.format("User with email %s not found", email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(mensagem));
    }
}
