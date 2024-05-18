package com.nofrontier.book.core.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import com.nofrontier.book.domain.exceptions.UserAlreadyRegisteredException;
import com.nofrontier.book.domain.exceptions.ValidatingException;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.domain.repository.UserRepository;

@Component
public class UserValidator {

	@Autowired
	private UserRepository repository;

	public void validate(User user) {
		validateEmail(user);
	}

	// ---------------------------------------------------------------------------------------------------------------------

	private void validateEmail(User user) {
		if (repository.isEmailAlreadyRegistered(user)) {
			var message = "User Registered Already exists with this e-mail";
			var fieldError = new FieldError(user.getClass().getName(), "email",
					user.getEmail(), false, null, null, message);
			throw new UserAlreadyRegisteredException(message, fieldError);
		}
		validateCpf(user);
	}

	// ---------------------------------------------------------------------------------------------------------------------

	private void validateCpf(User user) {
		List<Person> people = user.getPersons();
		if (people == null || people.isEmpty()) {
			return;
		}
		for (Person person : people) {
			if (person.getCpf() != null
					&& repository.isCpfAlreadyRegistered(person.getCpf())) {
				var message = "There is already a user registered with this cpf";
				var fieldError = new FieldError(person.getClass().getName(),
						"cpf", person.getCpf(), false, null, null, message);
				throw new UserAlreadyRegisteredException(message, fieldError);
			}
		}
		validateKeyPix(user);
	}

	// ---------------------------------------------------------------------------------------------------------------------

	private void validateKeyPix(User user) {
		List<Person> people = user.getPersons();
		if (people == null || people.isEmpty()) {
			return;
		}
		for (Person person : people) {
			if (person.getKeyPix() != null && repository
					.isKeyPixAlreadyRegistered(person.getKeyPix())) {
				var message = "There is already a user registered with this key pix";
				var fieldError = new FieldError(person.getClass().getName(),
						"keyPix", person.getKeyPix(), false, null, null,
						message);
				throw new UserAlreadyRegisteredException(message, fieldError);
			}
		}
		for (Person person : people) {
			if (user.isCustomer() && person.getKeyPix() == null) {
				var message = "User type Housekeeper must have a pix key";
				var fieldError = new FieldError(person.getClass().getName(),
						"keyPix", null, false, null, null, message);
				throw new ValidatingException(message, fieldError);
			}
		}
	}

}