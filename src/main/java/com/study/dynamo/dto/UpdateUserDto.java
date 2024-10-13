package com.study.dynamo.dto;

import lombok.Builder;

@Builder
public record UpdateUserDto(
        String username,
        String email,
        String cpf,
        String phoneNumber
) {

    @Override
    public String toString() {
        return "UpdateUserDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
