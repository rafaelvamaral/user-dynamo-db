package com.study.dynamo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateUserDto(
        @NotBlank(message = "'username' should not be null")
        String username,
        @NotBlank(message = "'email' should not be null")
        String email,
        @NotBlank(message = "'cpf' should not be null")
        String cpf,
        @NotBlank(message = "'phoneNumber' should not be null")
        String phoneNumber
) {
}
