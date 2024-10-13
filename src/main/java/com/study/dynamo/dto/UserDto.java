package com.study.dynamo.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDto(
        UUID uuid,
        String username,
        String email,
        String cpf,
        String phoneNumber
) {
}
