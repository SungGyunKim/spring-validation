package com.spring.validation.constraint;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.spring.validation.validator.AdMessageConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({TYPE}) // TYPE은 클래스, 인터페이스, Enum에 부착할 수 있게 한다는 의미이다.
@Retention(RUNTIME)
@Constraint(validatedBy = AdMessageConstraintValidator.class)
@Documented
public @interface AdMessageConstraint {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
