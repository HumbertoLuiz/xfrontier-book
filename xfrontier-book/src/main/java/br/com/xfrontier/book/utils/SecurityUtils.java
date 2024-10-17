package br.com.xfrontier.book.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.xfrontier.book.core.enums.UserType;
import br.com.xfrontier.book.domain.exceptions.UserNotFoundException;
import br.com.xfrontier.book.domain.model.User;
import br.com.xfrontier.book.domain.repository.UserRepository;

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

    public String getUsernameUserLogged() {
        return getAuthentication().getName();
    }

    public User getLoggedUser() {
        var email = getUsernameUserLogged();
        var mensagem = String.format("User with email %s not found", email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(mensagem));
    }
}
