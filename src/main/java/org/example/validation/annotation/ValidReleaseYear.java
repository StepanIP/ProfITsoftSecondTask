package org.example.validation.annotation;

import org.example.validation.ReleaseYearValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReleaseYearValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReleaseYear {
    String message() default "Release year must be between the first movie's release year and the current year";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
