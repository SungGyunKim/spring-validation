package com.spring.validation.model;

import com.spring.validation.enums.ContactType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Locale;

class CreateContactTests {
    @Test
    void test_validate() {
        Locale.setDefault(Locale.US);
        // Given
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        final CreateContact createContact = CreateContact
                .builder()
                .uid(null) // @NotBlank가 정의되어 있기때문에 null이 오면 안된다.
                .contact("000")
                .contactType(ContactType.PHONE_NUMBER)
                .build();

        // When
        final Collection<ConstraintViolation<CreateContact>> constraintViolations = validator.validate(createContact);

        // Then
        assertEquals(1, constraintViolations.size()); // ConstraintViolation에 실패에 대한 정보가 담긴다.
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }
}
