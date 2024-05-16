package com.spring.validation.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.validation.enums.ContactType;
import com.spring.validation.model.CreateContact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ContactControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * <h3>
   *   Controller에서 유효성 검사를 진행
   * </h3>
   *
   * <ol>
   *   <li>
   *     Controller 메서드의 파라미터에 {@code @Valid}를 넣어 준다.
   *   </li>
   * </ol>
   */
  @Test
  void controller() throws Exception {
    // given
    final CreateContact createContact = CreateContact
            .builder()
            .uid(null) // @NotBlank가 정의되어 있기 때문에 null이 오면 안 된다.
            .contact("000")
            .contactType(ContactType.PHONE_NUMBER)
            .build();

    // when & then
    mockMvc.perform(
        post("/contacts")
        .content(objectMapper.writeValueAsString(createContact))
        .contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().is4xxClientError());
  }

  /**
   * <h3>
   *   Controller에서 유효성 검사를 진행시 발생하는 Exception을 잡아 응답한다.
   * </h3>
   *
   * <ol>
   *   <li>
   *     {@code @RestControllerAdvice}를 Controller를 AOP로 잡는다.
   *   </li>
   *   <li>
   *     {@code @ExceptionHandler(MethodArgumentNotValidException.class)}로 {@code @RequestBody}로 변환시 실시하는 유효성 검사의 실패 사유를 잡는다.
   *   </li>
   *   <li>
   *     {@code @ExceptionHandler(ConstraintViolationException.class)}로 {@code @PathVariable}에 매핑되는 경로 파라미터 또는
   *     {@code @RequestParam}에 매핑되는 쿼리 파라미터를 변환시 실시하는 유효성 검사의 실패 사유를 잡는다.
   *   </li>
   * </ol>
   */
  @Test
  void errorHandling() throws Exception {
    // given
    final CreateContact createContact = CreateContact
            .builder()
            .uid(null) // @NotBlank가 정의되어 있기 때문에 null이 오면 안 된다.
            .contact("000")
            .contactType(ContactType.PHONE_NUMBER)
            .build();

    // when & then
    mockMvc.perform(
        post("/contacts")
        .content(objectMapper.writeValueAsString(createContact))
        .contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().is4xxClientError())
    .andExpect(content().string("{\"uid\":\"must not be blank\"}"));
  }
}