package ru.yandex.practicum.filmorate.exception;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PostureDateValidator.class)
@Documented
public @interface PostureDate {

    String message() default "{CapitalLetter.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

class PostureDateValidator implements ConstraintValidator<PostureDate, LocalDate> {
    private LocalDate localDate;
    private ConstraintValidatorContext context;

    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        this.localDate = localDate;
        this.context = context;
        if (localDate != null) {
            return LocalDate.of(1895, 12, 28).isBefore(localDate);
        }
        return true;
    }
}