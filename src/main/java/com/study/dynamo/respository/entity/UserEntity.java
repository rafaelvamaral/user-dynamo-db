package com.study.dynamo.respository.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
@EqualsAndHashCode
public class UserEntity {

    private UUID uuid;
    private String username;
    private String email;
    private String cpf;
    private String phoneNumber;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("user_id")
    public UUID getUuid() {
        return uuid;
    }

    @DynamoDbAttribute("username")
    public String getUsername() {
        return username;
    }

    @DynamoDbAttribute("email")
    public String getEmail() {
        return email;
    }

    @DynamoDbAttribute("cpf")
    public String getCpf() {
        return cpf;
    }

    @DynamoDbAttribute("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
