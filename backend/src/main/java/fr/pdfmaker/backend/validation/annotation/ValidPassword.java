package fr.pdfmaker.backend.validation.annotation;
import fr.pdfmaker.backend.validation.validator.PasswordValidator;

import java.util.regex.*;


import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Mot de passe invalide";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};



}
