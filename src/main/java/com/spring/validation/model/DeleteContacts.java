package com.spring.validation.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import lombok.Builder;

@Builder
public class DeleteContacts {
    @NotNull
    @Size(min = 1)
    private Collection<@Size(max = 64) @NotBlank String> uids;
}
