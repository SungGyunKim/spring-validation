package com.spring.validation.model;

import com.spring.validation.constraint.AdMessageConstraint;
import com.spring.validation.groups.Ad;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@AdMessageConstraint // 이 커스텀 제약을 구현할 것이다.
@Builder
@Getter
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
    private boolean isAd; // 광고 여부를 설정할 수 있는 속성
}
