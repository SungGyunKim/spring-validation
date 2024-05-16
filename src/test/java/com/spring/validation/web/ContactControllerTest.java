package com.spring.validation.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}