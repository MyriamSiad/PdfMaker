package fr.pdfmaker.backend.validation.annotation;


import fr.pdfmaker.backend.validation.validator.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String message() default "Format d'email invalide ! L'email doit être au format";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
