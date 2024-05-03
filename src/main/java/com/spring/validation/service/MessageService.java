package com.spring.validation.service;

import com.spring.validation.groups.Ad;
import com.spring.validation.model.Message;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class MessageService {
    @Validated(Ad.class) // 메서드 호출 시 Ad 그룹이 지정된 제약만 검사한다.
    public void sendAdMessage(@Valid Message message) {
        // Do Something
    }

    public void sendNormalMessage(@Valid Message message) {
        // Do Something
    }

    /**
     * 주의: 이렇게 호출하면 Spring AOP Proxy 구조상 @Valid를 설정한 메서드가 호출되어도 유효성 검사가 동작하지 않는다.
     * Spring의 AOP Proxy 구조에 대한 설명은 다음 링크를 참고하자.
     * - https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#aop-understanding-aop-proxies
     */
    public void sendMessage(Message message, boolean isAd) {
        if (isAd) {
            sendAdMessage(message);
        } else {
            sendNormalMessage(message);
        }
    }

    /**
     * message.isAd가 true이면 Ad 그룹에 속한 contcat, removeGuide 속성까지 검사한다.
     */
    public void sendMessage(@Valid Message message) {
         // Do Something
    }
}
