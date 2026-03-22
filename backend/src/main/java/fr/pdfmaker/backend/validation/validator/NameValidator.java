package fr.pdfmaker.backend.validation.validator;

import fr.pdfmaker.backend.validation.annotation.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName , String > {



    @Override
    public void initialize(ValidName constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value , ConstraintValidatorContext context) {
        if (value == null || value.isBlank())
            return false;

        if (value.length() < 2 || value.length() > 50)
            return false;

        // doit contenir au moins une lettre
        if (!value.matches(".*\\p{L}.*"))
            return false;

        // éviter spam
        if (value.matches("^(.)\\1+$"))
            return false;

        return true;
    }
}
