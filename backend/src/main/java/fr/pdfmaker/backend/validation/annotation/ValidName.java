package fr.pdfmaker.backend.validation.annotation;
import java.lang.annotation.*;

import fr.pdfmaker.backend.validation.validator.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {

    String message() default "Format invalide !";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};



}
