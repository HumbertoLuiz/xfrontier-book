package com.nofrontier.book.core.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import com.nofrontier.book.domain.exceptions.UserAlreadyRegisteredException;
import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.domain.repository.UserRepository;

@Component
public class UserValidator {

	@Autowired
	private UserRepository repository;

	public void validate(User user) {
		validateUsername(user);
	}

	// ---------------------------------------------------------------------------------------------------------------------

	private void validateUsername(User user) {
		if (repository.isEmailAlreadyRegistered(user)) {
			var message = "User Registered Already exists with this username";
			var fieldError = new FieldError(user.getClass().getName(), "username",
					user.getEmail(), false, null, null, message);
			throw new UserAlreadyRegisteredException(message, fieldError);
		}
		
		//validateCpf(user);
	}

	// ---------------------------------------------------------------------------------------------------------------------

//	private void validateCpf(User user) {
//		Person person = user.getPerson(); // Obter a pessoa associada ao usuário
//		if (person == null || person.getCpf() == null) {
//			return; // Se a pessoa ou CPF não estiver definido, não há
//					// necessidade de validação
//		}
//		// Verificar se o CPF já está registrado
//		if (repository.isCpfAlreadyRegistered(person.getCpf())) {
//			String message = "There is already a user registered with this CPF";
//			FieldError fieldError = new FieldError(person.getClass().getName(),
//					"cpf", person.getCpf(), false, null, null, message);
//			throw new UserAlreadyRegisteredException(message, fieldError);
//		}
//
//		// Chamada para outra validação
//		validateKeyPix(user);
//	}

	// ---------------------------------------------------------------------------------------------------------------------

//	private void validateKeyPix(User user) {
//		Person person = user.getPerson(); // Obter a pessoa associada ao usuário
//		if (person == null) {
//			return; // Se a pessoa não estiver definida, não há necessidade de
//					// validação
//		}
//		// Verificar se a chave Pix já está registrada
//		if (person.getKeyPix() != null
//				&& repository.isKeyPixAlreadyRegistered(person.getKeyPix())) {
//			String message = "There is already a user registered with this key pix";
//			FieldError fieldError = new FieldError(person.getClass().getName(),
//					"keyPix", person.getKeyPix(), false, null, null, message);
//			throw new UserAlreadyRegisteredException(message, fieldError);
//		}
//		// Verificar se o usuário do tipo CUSTOMER tem uma chave Pix
//		if (user.isCustomer() && person.getKeyPix() == null) {
//			String message = "User type Customer must have a pix key";
//			FieldError fieldError = new FieldError(person.getClass().getName(),
//					"keyPix", null, false, null, null, message);
//			throw new ValidatingException(message, fieldError);
//		}
//	}

}
