package com.study.dynamo.unit;

import com.study.dynamo.respository.DefaultUserRepository;
import com.study.dynamo.respository.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryUnitTest {

    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Mock
    private DynamoDbTable<UserEntity> dynamoDbTable;

    @InjectMocks
    private DefaultUserRepository userRepository;

    @BeforeEach
    public void setUp() {
        Mockito.when(dynamoDbEnhancedClient.table(anyString(),
                Mockito.eq(TableSchema.fromBean(UserEntity.class)))).thenReturn(dynamoDbTable);
    }

    @Test
    public void givenAnValidUuid_WhenCallsFindUserByUuidThenReturnOptionalUserEntity() {

        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var expectedUserEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        Mockito.when(dynamoDbTable.getItem(any(Key.class))).thenReturn(expectedUserEntity);

        var response = userRepository.findByUuid(expectedUuid).get();

        Assertions.assertEquals(expectedUserEntity, response);

        Mockito.verify(dynamoDbTable, times(1)).getItem(any(Key.class));

    }

    @Test
    public void givenAnInvalidUuid_WhenCallsFindUserByUuid_ThenReturnEmptyOptional() {

        var nonExistentUuid = UUID.randomUUID();

        Mockito.when(dynamoDbTable.getItem(any(Key.class))).thenReturn(null);

        var response = userRepository.findByUuid(nonExistentUuid);

        Assertions.assertTrue(response.isEmpty());
        Mockito.verify(dynamoDbTable, times(1)).getItem(any(Key.class));
    }

    @Test
    public void givenAUserEntity_WhenCallsSave_ThenPutItemIsCalled() {

        var userEntity = UserEntity.builder()
                .uuid(UUID.randomUUID())
                .username("Test User")
                .email("test@user.com")
                .cpf("12345678900")
                .phoneNumber("1234567890")
                .build();

        userRepository.save(userEntity);

        Mockito.verify(dynamoDbTable, times(1)).putItem(userEntity);
    }

    @Test
    public void givenAUserEntity_WhenCallsPatch_ThenUpdateItemIsCalledAndReturnUpdatedEntity() {
        var userEntity = UserEntity.builder()
                .uuid(UUID.randomUUID())
                .username("Updated User")
                .email("updated@user.com")
                .cpf("98765432100")
                .phoneNumber("0987654321")
                .build();

        Mockito.when(dynamoDbTable.updateItem(userEntity)).thenReturn(userEntity);

        var updatedUser = userRepository.patch(userEntity);

        Assertions.assertEquals(userEntity, updatedUser);
        Mockito.verify(dynamoDbTable, times(1)).updateItem(userEntity);
    }

    @Test
    public void givenAUuid_WhenCallsDelete_ThenDeleteItemIsCalled() {
        var uuidToDelete = UUID.randomUUID();

        userRepository.delete(uuidToDelete);

        Mockito.verify(dynamoDbTable, times(1)).deleteItem(any(Key.class));
    }
}
