package com.nofrontier.book.domain.services;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nofrontier.book.domain.model.User;

public class AuthUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    User user;
    
	public AuthUser(User user) {
		this.user = user;
	}

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return user.getGroups().stream()
                .flatMap(group -> group.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getAuthority().toUpperCase()))
                .collect(Collectors.toSet());
    }

//    @Override
//    public Collection<GrantedAuthority> getAuthorities() {
//        return user.getPermissions().stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getAuthority().toUpperCase()))
//                .collect(Collectors.toSet());
//    }

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return user.getPermissions().stream()
//              .map(permission -> new SimpleGrantedAuthority(permission.getAuthority().toUpperCase()))
//              .collect(Collectors.toSet());
//	}
    
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
