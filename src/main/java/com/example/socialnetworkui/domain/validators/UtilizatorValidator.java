package com.example.socialnetworkui.domain.validators;
import com.example.socialnetworkui.domain.User;

public class UtilizatorValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getFirstName().equals(""))
            throw new ValidationException("Utilizatorul nu este valid");
        if(!entity.getEmail().contains("@"))
            throw new ValidationException("Adresa de email nu este valida");
    }
}
