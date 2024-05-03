package com.spring.validation.validator;

import com.spring.validation.constraint.AdMessageConstraint;
import com.spring.validation.groups.Ad;
import com.spring.validation.model.Message;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

public class AdMessageConstraintValidator implements
    ConstraintValidator<AdMessageConstraint, Message> {
    private final Validator validator;

    public AdMessageConstraintValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(Message value, ConstraintValidatorContext context) {
        if (value.isAd()) {
            final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(value, Ad.class);
            if (CollectionUtils.isNotEmpty(constraintViolations)) {
                context.disableDefaultConstraintViolation(); // 기본 메시지 제거
                // 기본 메시지를 제거 했기 때문에 메시지를 새로 구성한다.
                constraintViolations.forEach(constraintViolation -> context
                        .buildConstraintViolationWithTemplate(constraintViolation.getMessageTemplate())
                        .addPropertyNode(constraintViolation.getPropertyPath().toString())
                        .addConstraintViolation());
                return false; // false인 경우에 exception을 발생 시킨다.
            }
        }

        return true;
    }
}
