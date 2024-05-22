package com.nofrontier.book.auth.models;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nofrontier.book.domain.model.Permission;
import com.nofrontier.book.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Autowired
	private User user;

	// @Override
	// public Collection<? extends GrantedAuthority> getAuthorities() {
	// return AuthorityUtils
	// .createAuthorityList(user.getPermissions().toString());
	// }
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getPermissions().stream().map(
				permission -> new SimpleGrantedAuthority(permission.getName()))
				.collect(Collectors.toList());
	}

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
		return true;
	}

	public AuthUser() {
	}

	public void addRole(Permission permission) {
	}

}
