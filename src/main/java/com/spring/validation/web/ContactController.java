package com.spring.validation.web;

import com.spring.validation.model.CreateContact;
import com.spring.validation.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ContactController {
  private final ContactService contactService;

    @PostMapping("/contacts")
    public ResponseEntity<String> createContact(@Valid CreateContact createContact) { // 메서드 호출 시 유효성 검사 진행
      return ResponseEntity.ok("success");
    }
}
