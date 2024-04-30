package com.spring.validation.service;

import com.spring.validation.model.CreateContact;
import com.spring.validation.model.DeleteContacts;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated // 여기에 추가(AOP에서 처리)
@Service
public class ContactService {
    public void createContact(@Valid CreateContact createContact) { // '@Valid'가 설정된 메서드가 호출될 때 유효성 검사를 진행한다.
        // Do Something
    }

    public void deleteContacts(@Valid DeleteContacts deleteContact) {
        // Do Something
    }
}
