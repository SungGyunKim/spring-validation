package com.spring.validation.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spring.validation.enums.ContactType;
import com.spring.validation.service.ContactService;
import com.spring.validation.service.MessageService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Locale;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class CreateContactTests {
    @Autowired
    private ContactService contactService;

    @Autowired
    private MessageService messageService;

    /**
     * <h3>
     *   {@link jakarta.validation.Validator}를 통해 DTO의 유효성 검사를 수동({@code validator.validate(T)})으로 진행
     * </h3>
     *
     * <ol>
     *   <li>
     *     {@code Validation.buildDefaultValidatorFactory().getValidator()}를 호출하여 {@link jakarta.validation.Validator}를 획득한다.
     *   </li>
     *   <li>
     *     {@code validator.validate(T)}를 호출하여 유효성 검사를 진행한다.
     *   </li>
     *   <li>
     *     반환된 실패 사유({@code Set<ConstraintViolation<T>>})를 확인할 수 있다.
     *   </li>
     * </ol>
     */
    @Test
    void basic() {
        Locale.setDefault(Locale.US);
        // Given
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        final CreateContact createContact = CreateContact
                .builder()
                .uid(null) // @NotBlank가 정의되어 있기 때문에 null이 오면 안 된다.
                .contact("000")
                .contactType(ContactType.PHONE_NUMBER)
                .build();

        // When
        final Set<ConstraintViolation<CreateContact>> constraintViolations = validator.validate(createContact);

        // Then
        assertEquals(1, constraintViolations.size()); // ConstraintViolation에 실패에 대한 정보가 담긴다.
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    /**
     * <h3>
     *   {@code @Validated}를 통해 Service layer 에서 AOP를 통해 DTO의 유효성 검사를 진행
     * </h3>
     *
     * <ol>
     *   <li>
     *     Class level에서 @Validated를 달아 준다.
     *   </li>
     *   <li>
     *     Method level에서 DTO에 @Valid를 달아 준다.
     *   </li>
     *   <li>
     *     Spring AOP를 통해 유효성 검사를 진행되고 실패하면 {@link ConstraintViolationException}를 발생시킨다.
     *   </li>
     *   <li>
     *     {@code exception.getConstraintViolations()}를 호출하여 실패 사유를 확인할 수 있다.
     *   </li>
     * </ol>
     */
    @Test
    void service() {
        Locale.setDefault(Locale.US);
        // Given
        final CreateContact createContact = CreateContact
                .builder()
                .uid(null) // @NotBlank가 정의되어 있기 때문에 null이 오면 안 된다.
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

    /**
     * <h3>
     *   {@code Collection} 속에 있는 값에 대해 유효성 검사를 진행
     * </h3>
     *
     * <ol>
     *   <li>
     *     Generic Type 앞에 {@code @Size, @NotBlank}와 같은 제약 조건들을 추가한다.
     *   </li>
     * </ol>
     */
    @Test
    void collection() {
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

    /**
     * <h3>
     *   사용자 정의 제약 조건을 만들어 유효성 검사를 진행
     * </h3>
     *
     * <ol>
     *   <li>
     *     {@link jakarta.validation.ConstraintValidator}를 상속 받아 유효성 검사기를 만든다.<br>
     *     {@code isValid()}를 재정의하여 유효성 검사 로직을 넣는다.<br>
     *     예제) {@link com.spring.validation.validator.NoEmojiValidator}
     *   </li>
     *   <li>
     *     유효성 검사 대상 어노테이션을 만들고 유효성 검사기를 지정한다.<br>
     *     유효성 검사 대상 어노테이션은 정해진 규칙에 맞춰 작성되어야 한다.<br>
     *     <ul>
     *       <li>
     *         {@code @Constraint}에 유효성 검사기를 지정한다.
     *       </li>
     *       <li>
     *         {@code String message() default "default message";}와 같이 유효성 실패 메시지 변수를 만든다.<br>
     *         기본적으로 문자열도 가능하지만 국제화를 위해 resource bundle key로 작성할 것을 추천한다.<br>
     *         이 때, resource bundle key는 FQCN 뒤에 ".message"를 작성하는 컨벤션을 따르는 것을 추천한다.<br>
     *         예제) {@link jakarta.validation.constraints.NotBlank}
     *       </li>
     *       <li>
     *         {@code Class<?>[] groups() default {};}와 같이 유효성 검사 그룹을 변수를 만든다.
     *       </li>
     *       <li>
     *         {@code Class<? extends Payload>[] payload() default {};} 유효성 검사기에서 사용할 수 있는 메타 데이터를 지정하는 변수를 만든다.
     *       </li>
     *     </ul>
     *     예제) {@link com.spring.validation.constraint.NoEmoji}
     *   </li>
     * </ol>
     */
    @Test
    void customConstraint() {
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

    /**
     * <h3>
     *   기본 그룹에 대한 유효성 검사를 진행
     * </h3>
     *
     * <ol>
     *   <li>
     *      Controller, Service의 메서드에 그룹을 지정하지 않으면 그룹이 없는 제약조건에 대한 유효성 검사를 진행한다.
     *   </li>
     * </ol>
     */
    @Test
    void defaultGroup() {
        Locale.setDefault(Locale.US);
        // Given
        final Message message = Message.builder()
            .title("title")
            .build();

        // When
        try {
            messageService.sendNormalMessage(message);
        } catch (ConstraintViolationException exception) {
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

            // Then
            assertEquals(1, constraintViolations.size());
            assertEquals("sendNormalMessage.message.body: must not be empty", exception.getMessage());
        }
    }

    /**
     * <h3>특정 그룹에 지정된 항목만 유효성 검사를 진행</h3>
     *
     * <ol>
     *   <li>
     *     그룹을 지정하기 위한 마커 인터페이스(Marker Interface)를 만든다.<br>
     *     예제) {@link com.spring.validation.groups.Ad}
     *   </li>
     *   <li>
     *     제약조건의 {@code groups}에 그룹을 할당한다.<br>
     *     예제) {@link com.spring.validation.model.Message}
     *   </li>
     *   <li>
     *     Service의 메서드에 {@code @Validated}을 넣고 그룹을 지정한다.<br>
     *     예제) {@link com.spring.validation.service.MessageService#sendAdMessage(Message)}
     *   </li>
     * </ol>
     */
    @Test
    void customGroup() {
        Locale.setDefault(Locale.US);
        // Given
        final Message message = Message.builder()
            .contact("000")
            .build();

        // When
        try {
            messageService.sendAdMessage(message);
        } catch (ConstraintViolationException exception) {
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

            // Then
            assertEquals(1, constraintViolations.size());
            assertEquals("sendAdMessage.message.removeGuide: must not be empty", exception.getMessage());
        }
    }

    /**
     * <h3>
     *   클래스 레벨에서 여러 필드를 사용하여 유효성 검사를 진행<br>
     *   예를들어 특정 필드의 값이 true일 때, 특정 그룹으로 유효성 검사를 하거나
     *   from이 to보다 작거나 같아야 하는 값의 전후 관계에 대한 유효성 검사를 할 수 있다.<br>
     *   ※ <b>필드 레벨에서 유효성 검사 후 클래스 레벨에서 추가로 유효성 검사</b>하는 것이기 때문에 실패 사유에 모두 포함된다.
     * </h3>
     *
     * <ol>
     *   <li>
     *      '사용자 정의 제약 조건'을 만들듯이 유효성 검사 대상과 검사기를 만든다.<br>
     *      예제) {@link com.spring.validation.constraint.AdMessageConstraint}<br>
     *      예제) {@link com.spring.validation.validator.AdMessageConstraintValidator}<br>
     *      ※ 클래스 레벨이므로 {@code @Target}에 {@code TYPE}으로 설정해야 한다.
     *   </li>
     *   <li>
     *     유효성 검사 대상에 클래스 레벨로 붙인다.
     *   </li>
     * </ol>
     */
    @Test
    void classLevel() {
        Locale.setDefault(Locale.US);
        // Given
        final Message message = Message.builder()
            .isAd(true)
            .contact("000")
            .build();

        // When
        try {
            messageService.sendMessage(message);
        } catch (ConstraintViolationException exception) {
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

            // Then
            assertEquals(3, constraintViolations.size());
        }
    }
}
