package com.spring.validation.model;

import com.spring.validation.groups.Ad;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class Message {
    @Size(max = 128)
    @NotEmpty
    private String title;
    @Size(max = 1024)
    @NotEmpty
    private String body;
    @Size(max = 32, groups = Ad.class)
    @NotEmpty(groups = Ad.class)  // 그룹을 지정할 수 있다. (기본 값은 javax.validation.groups.Default)
    private String contact;
    @Size(max = 64, groups = Ad.class)
    @NotEmpty(groups = Ad.class)
    private String removeGuide;
}
