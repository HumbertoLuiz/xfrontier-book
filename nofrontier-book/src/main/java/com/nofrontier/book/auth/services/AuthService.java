package com.nofrontier.book.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nofrontier.book.auth.models.AuthUser;
import com.nofrontier.book.domain.repository.UserRepository;

@Service
public class AuthService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		var mensagem = String.format("User with email %s not found", email);

		return userRepository.findByEmail(email).map(AuthUser::new)
				.orElseThrow(() -> new UsernameNotFoundException(mensagem));
	}

}