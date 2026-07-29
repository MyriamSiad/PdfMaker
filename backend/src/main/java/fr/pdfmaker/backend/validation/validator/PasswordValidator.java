package fr.pdfmaker.backend.validation.validator;
import fr.pdfmaker.backend.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {


    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{12,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;
        return pattern.matcher(password).matches();
    }

}

