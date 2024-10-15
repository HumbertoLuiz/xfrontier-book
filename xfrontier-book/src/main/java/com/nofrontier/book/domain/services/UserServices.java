package com.nofrontier.book.domain.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nofrontier.book.domain.repository.UserRepository;


@Service
public class UserServices implements UserDetailsService {

	private Logger logger = Logger.getLogger(UserServices.class.getName());

	@Autowired
	UserRepository userRepository;

	public UserServices(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		logger.info("Finding one user by email " + email + "!");
		var message = String.format("User with email %s not found", email);

		return userRepository.findByEmail(email).map(AuthUser::new)
				.orElseThrow(() -> new UsernameNotFoundException(message));
	}

}
