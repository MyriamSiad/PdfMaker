package fr.pdfmaker.backend.validation.validator;

import fr.pdfmaker.backend.validation.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail , String > {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value , ConstraintValidatorContext context) {
        if (value == null || value.isBlank())
            return false;

        // regex pour valider le format de l'email
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return value.matches(emailRegex);
    }
}
