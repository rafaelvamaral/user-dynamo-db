package com.study.dynamo.unit;

import com.study.dynamo.dto.CreateUserDto;
import com.study.dynamo.dto.UpdateUserDto;
import com.study.dynamo.exception.UserNotFoundException;
import com.study.dynamo.exception.UserNotPatchedException;
import com.study.dynamo.mapper.UserMapper;
import com.study.dynamo.respository.UserRepository;
import com.study.dynamo.respository.entity.UserEntity;
import com.study.dynamo.service.DefaultUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DefaultUserService userService;


    // 1. Teste feliz ao encontrar usuario
    // 2. Teste de erro ao encontrar usuario
    // 3. teste feliz de atualizar usuario
    // 4. testes de erro ao atualizar usuario
    // 5. teste feliz salvar usuario
    // 6. teste feliz ao deletar usuairo

    @Test
    public void givenAValidUuid_whenCallsFindUserByUuid_shouldReturnUserDto() {

        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var aUserEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.ofNullable(aUserEntity));

        Assertions.assertDoesNotThrow(() -> userService.findUserByUuid(expectedUuid));

        verify(userRepository, times(1)).findByUuid(any(UUID.class));
    }

    @Test
    public void givenAnInvalidUuid_WhenCallsFindUserByUuid_shouldThrowsUserNotFoundException() {

        var expectedUuid = UUID.randomUUID();

        when(userRepository.findByUuid(any(UUID.class))).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserByUuid(expectedUuid));

        verify(userRepository, times(1)).findByUuid(any(UUID.class));

    }

    @Test
    public void givenAnValidUuid_WhenCallsUpdateUser_shouldReturnUserDto() {

        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var aUserEntityFound = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        var expectedUpdatedUsername = "João da Silva Updated";

        var aUserEntityUpdated = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUpdatedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.ofNullable(aUserEntityFound));

        when(userRepository.patch(any(UserEntity.class))).thenReturn(aUserEntityUpdated);

        var updateUserDto = UpdateUserDto.builder().username(expectedUpdatedUsername).build();

        var updatedUser = Assertions.assertDoesNotThrow(() -> userService.patchUser(expectedUuid, updateUserDto));

        Assertions.assertEquals(expectedUuid, updatedUser.uuid());
        Assertions.assertEquals(expectedUpdatedUsername, updatedUser.username());
        Assertions.assertEquals(expectedEmail, updatedUser.email());
        Assertions.assertEquals(expectedCpf, updatedUser.cpf());
        Assertions.assertEquals(expectedPhoneNumber, updatedUser.phoneNumber());

        verify(userRepository, times(1)).findByUuid(any(UUID.class));
        verify(userRepository, times(1)).patch(any(UserEntity.class));

    }

    @Test
    public void givenAnInvalidUuid_WhenCallsCallsUpdateUser_shouldThrowsException() {

        var expectedUuid = UUID.randomUUID();
        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var aUserEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        var expectedUpdatedUsername = "João da Silva Updated";

        var updateUserDto = UpdateUserDto.builder().username(expectedUpdatedUsername).build();

        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.ofNullable(aUserEntity));

        when(userRepository.patch(any(UserEntity.class))).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(UserNotPatchedException.class, () -> userService.patchUser(expectedUuid, updateUserDto));

        verify(userRepository, times(1)).findByUuid(any(UUID.class));

    }

    @Test
    public void givenACreateUserDto_whenCallsCreateUser_shouldReturnUserDto() {

        var expectedUsername = "João da Silva";
        var expectedEmail = "joao.silva@email.com";
        var expectedCpf = "88661696097";
        var expectedPhoneNumber = "11988435898";

        var aCreateUserDto = CreateUserDto.builder()
                .username(expectedUsername)
                .email(expectedEmail)
                .cpf(expectedCpf)
                .phoneNumber(expectedPhoneNumber)
                .build();

        var userCreated = Assertions.assertDoesNotThrow(() -> userService.createUser(aCreateUserDto));

        Assertions.assertNotNull(userCreated.uuid());
        Assertions.assertEquals(expectedUsername, userCreated.username());
        Assertions.assertEquals(expectedEmail, userCreated.email());
        Assertions.assertEquals(expectedCpf, userCreated.cpf());
        Assertions.assertEquals(expectedPhoneNumber, userCreated.phoneNumber());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void givenAValidUuid_whenCallsDeleteUser_shouldReturnVoid() {

        var expectedUuid = UUID.randomUUID();

        Assertions.assertDoesNotThrow(() -> userService.deleteUser(expectedUuid));

        verify(userRepository, times(1)).delete(any(UUID.class));
    }

}
