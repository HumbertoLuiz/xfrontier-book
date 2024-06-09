package com.nofrontier.book.core.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import com.nofrontier.book.domain.exceptions.PersonAlreadyRegisteredException;
import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.repository.PersonRepository;

@Component
public class PersonValidator {

    @Autowired
    private PersonRepository repository;

    public void validate(Person person) {
        validateCpf(person);
    }

     private void validateCpf(Person person) {
        if (repository.isCpfAlreadyRegistered(person)) {
            var message = "There is already a user registered with this cpf";
            var fieldError = new FieldError(person.getClass().getName(), "cpf", person.getCpf(), false, null, null, message);

            throw new PersonAlreadyRegisteredException(message, fieldError);
        }

        validateKeyPix(person);
    }

    private void validateKeyPix(Person person) {
        if (repository.isKeyPixAlreadyRegistered(person)) {
            var message = "There is already a user registered with this pix key";
            var fieldError = new FieldError(person.getClass().getName(), "keyPix", person.getKeyPix(), false, null, null, message);

            throw new PersonAlreadyRegisteredException(message, fieldError);
        }
    }

}
