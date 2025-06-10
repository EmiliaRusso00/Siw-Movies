package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.User;

@Component
public class UserValidator implements Validator {

    final Integer MAX_NAME_LENGTH = 100;
    final Integer MIN_NAME_LENGTH = 2;

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        //rimuove gli spazi
        String name = user.getName().trim();
        String surname = user.getSurname().trim();

        if (name.isEmpty())
            errors.rejectValue("nome", "required");
        else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
            errors.rejectValue("nome", "size");

        if (surname.isEmpty())
            errors.rejectValue("cognome", "required");
        else if (surname.length() < MIN_NAME_LENGTH || surname.length() > MAX_NAME_LENGTH)
            errors.rejectValue("cognome", "size");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

}

