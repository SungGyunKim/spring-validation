package com.spring.validation.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spring.validation.enums.ContactType;
import com.spring.validation.service.ContactService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CreateContactTests {
    @Autowired
    private ContactService contactService;

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

    @Test
    void test_serviceValidate() {
        Locale.setDefault(Locale.US);
        // Given
        final CreateContact createContact = CreateContact
                .builder()
                .uid(null) // @NotBlank가 정의되어 있기때문에 null이 오면 안된다.
                .contact("000")
                .contactType(ContactType.PHONE_NUMBER)
                .build();

        // When
        try {
            contactService.createContact(createContact);
        } catch (ConstraintViolationException exception) {
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

            // Then
            assertEquals(1, constraintViolations.size()); // ConstraintViolation에 실패에 대한 정보가 담긴다.
            assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
        }
    }

    @Test
    void test_collectionValidate() {
        Locale.setDefault(Locale.US);
        // Given
        final DeleteContacts deleteContacts = DeleteContacts
            .builder()
            .build();

        // When
        try {
            contactService.deleteContacts(deleteContacts);
        } catch (ConstraintViolationException exception) {
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

            // Then
            assertEquals(1, constraintViolations.size());
            assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
        }


    }

    @Test
    void test_customValidate() {
        Locale.setDefault(Locale.US);
        // Given
        final CreateContact createContact = CreateContact
                .builder()
                .uid("\uD83D\uDE03") // 😃
                .contact("000")
                .contactType(ContactType.PHONE_NUMBER)
                .build();

        // When
        try {
            contactService.createContact(createContact);
        } catch (ConstraintViolationException exception) {
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

            // Then
            assertEquals(1, constraintViolations.size());
            assertEquals("Emoji is not allowed", constraintViolations.iterator().next().getMessage());
        }
    }
}
