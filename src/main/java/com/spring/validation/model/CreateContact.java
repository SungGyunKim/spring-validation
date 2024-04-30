package com.spring.validation.model;

import com.spring.validation.constraint.NoEmoji;
import com.spring.validation.enums.ContactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class CreateContact {
    @NoEmoji
    @Size(max = 64) // 최대 길이 64
    @NotBlank // 빈문자열은 안됨
    private String uid;
    @NotNull // null 안됨
    private ContactType contactType;
    @Size(max = 1_600) // 최대 길이 1,600
    private String contact;
}
