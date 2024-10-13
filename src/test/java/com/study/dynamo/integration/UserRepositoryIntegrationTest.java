package com.study.dynamo.integration;

import com.study.dynamo.respository.UserRepository;
import com.study.dynamo.respository.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private static DynamoDbClient dynamoDbClient;

    @Autowired
    private DynamoDbTable<UserEntity> dynamoDbTable;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenAValidUuid_WhenCallsFindByUuid_ThenReturnUserEntity() {
        // Given
        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var userEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        dynamoDbTable.putItem(userEntity);

        // When
        Optional<UserEntity> result = userRepository.findByUuid(expectedUuid);

        // Then
        assertTrue(result.isPresent());
        assertEquals(userEntity, result.get());
    }

    @Test
    public void givenAnInValidUuid_WhenCallsFindByUuid_ThenReturnsEmptyOptional() {
        // Given
        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var userEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        dynamoDbTable.putItem(userEntity);

        var notExpectedUuid = UUID.randomUUID();

        // When
        Optional<UserEntity> result = userRepository.findByUuid(notExpectedUuid);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenAValidUserEntity_WhenCallsSave_thenReturnsVoid() {

        // Given
        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var userEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        // When
        userRepository.save(userEntity);

        // Then
        var result = userRepository.findByUuid(expectedUuid);
        assertEquals(userEntity, result.get());
    }

    @Test
    public void givenAValidUuid_WhenCallsPatch_thenReturnsUserEntity() {

        // Given
        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var userEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        var expectedUpdateUsername = "João da Silva Updated";

        // When
        dynamoDbTable.putItem(userEntity);

        userEntity.setUsername("João da Silva Updated");

        var result = userRepository.patch(userEntity);

        // Then
        assertEquals(userEntity, result);
        assertEquals(expectedUpdateUsername, result.getUsername());
    }

    @Test
    public void givenAValidUuid_WhenCallsDelete_thenReturnsVoid() {

        // Given
        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var userEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        // When
        dynamoDbTable.putItem(userEntity);

        userRepository.delete(expectedUuid);

        // Then
        var result = userRepository.findByUuid(expectedUuid);
        assertTrue(result.isEmpty());
    }
}