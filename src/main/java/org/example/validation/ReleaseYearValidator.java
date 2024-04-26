package org.example.validation;

import org.example.validation.annotation.ValidReleaseYear;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseYearValidator implements ConstraintValidator<ValidReleaseYear, Integer> {

    @Override
    public void initialize(ValidReleaseYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        int currentYear = LocalDate.now().getYear();
        int firstMovieReleaseYear = 1895;
        
        return year >= firstMovieReleaseYear && year <= currentYear;
    }
}
