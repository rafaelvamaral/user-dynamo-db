package com.study.dynamo.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.dynamo.dto.CreateUserDto;
import com.study.dynamo.dto.UpdateUserDto;
import com.study.dynamo.dto.UserDto;
import com.study.dynamo.exception.UserNotCreatedException;
import com.study.dynamo.exception.UserNotDeletedException;
import com.study.dynamo.exception.UserNotFoundException;
import com.study.dynamo.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@WebMvcTest
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void givenAValidUuid_whenCallsGetUser_shouldReturnUser() throws Exception {

        //given
        var expectedUuid = UUID.randomUUID();
        var expectedCpf = "123456789";
        var expectedEmail = "joao@silva.com";
        var expectedPhoneNumber = "119878674768";
        var expectedUsername = "joao.silva";

        var userDto = UserDto.builder()
                .uuid(expectedUuid)
                .cpf(expectedCpf)
                .email(expectedEmail)
                .phoneNumber(expectedPhoneNumber)
                .username(expectedUsername)
                .build();

        Mockito.when(userService.findUserByUuid(Mockito.any(UUID.class)))
                .thenReturn(userDto);

        //when
        final var request = MockMvcRequestBuilders.get("/users/{uuid}", expectedUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(expectedUuid.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf", Matchers.equalTo(expectedCpf)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.equalTo(expectedEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Matchers.equalTo(expectedPhoneNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo(expectedUsername)));

        Mockito.verify(userService, Mockito.times(1)).findUserByUuid(ArgumentMatchers.eq(expectedUuid));
    }

    @Test
    public void givenAnInvalidUuid_whenCallsGetUser_shouldReturnNotFoundException() throws Exception {

        //given
        final var expectedUuid = UUID.randomUUID();

        Mockito.when(userService.findUserByUuid(Mockito.any(UUID.class))).thenThrow(new UserNotFoundException("Not found", "User not found"));

        //when
        final var request = MockMvcRequestBuilders.get("/users/{uuid}", expectedUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_title", Matchers.equalTo("Not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description", Matchers.equalTo("User not found")));

        Mockito.verify(userService, Mockito.times(1)).findUserByUuid(ArgumentMatchers.eq(expectedUuid));
    }

    @Test
    public void givenAValidRequest_whenCallsCreateUser_shouldReturnUser() throws Exception {

        //given
        var expectedCpf = "123456789";
        var expectedEmail = "joao@silva.com";
        var expectedPhoneNumber = "119878674768";
        var expectedUsername = "joao.silva";

        var createUserDto = CreateUserDto.builder()
                .cpf(expectedCpf)
                .email(expectedEmail)
                .phoneNumber(expectedPhoneNumber)
                .username(expectedUsername)
                .build();

        var userDtoUuid = UUID.randomUUID();

        var userDto = UserDto.builder()
                .uuid(userDtoUuid)
                .cpf(expectedCpf)
                .email(expectedEmail)
                .phoneNumber(expectedPhoneNumber)
                .username(expectedUsername)
                .build();

        Mockito.when(userService.createUser(createUserDto)).thenReturn(userDto);

        //when
        final var request = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto));

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(userDtoUuid.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf", Matchers.equalTo(expectedCpf)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.equalTo(expectedEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Matchers.equalTo(expectedPhoneNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo(expectedUsername)));

        Mockito.verify(userService, Mockito.times(1)).createUser(ArgumentMatchers.eq(createUserDto));
    }

    @Test
    public void givenAnInValidRequest_whenCallsCreateUser_shouldReturnMethodArgumentNotValidException() throws Exception {

        //given
        var expectedCpf = "123456789";
        var expectedEmail = "joao@silva.com";
        var expectedPhoneNumber = "119878674768";

        var createUserDto = CreateUserDto.builder()
                .cpf(expectedCpf)
                .email(expectedEmail)
                .phoneNumber(expectedPhoneNumber)
                .build();

        //when
        final var request = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto));

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_title", Matchers.equalTo("Parâmetros incorretos na requisição")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", Matchers.equalTo("'username' should not be null")));

        Mockito.verify(userService, Mockito.times(0)).createUser(ArgumentMatchers.eq(createUserDto));
    }

    @Test
    public void givenAnInValidRequest_whenCallsCreateUser_shouldReturnUserNotCreatedException() throws Exception {

        //given
        var expectedCpf = "123456789";
        var expectedEmail = "joao@silva.com";
        var expectedPhoneNumber = "119878674768";
        var expectedUsername = "joao.silva";

        var createUserDto = CreateUserDto.builder()
                .cpf(expectedCpf)
                .email(expectedEmail)
                .phoneNumber(expectedPhoneNumber)
                .username(expectedUsername)
                .build();

        Mockito.when(userService.createUser(createUserDto)).thenThrow(new UserNotCreatedException("Erro ao criar usuário",
                String.format("Não foi possível criar usuário com username: %s", expectedUsername)));

        //when
        final var request = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto));

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_title", Matchers.equalTo("Erro ao criar usuário")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description", Matchers.equalTo(String.format("Não foi possível criar usuário com username: %s", expectedUsername))));

        Mockito.verify(userService, Mockito.times(1)).createUser(ArgumentMatchers.eq(createUserDto));
    }

    @Test
    public void givenAValidRequest_whenCallsPatchUser_shouldReturnUser() throws Exception {

        //given
        var expectedUuid = UUID.randomUUID();
        var expectedCpf = "123456789";
        var expectedEmail = "joao@silva.com";
        var expectedPhoneNumber = "119878674768";
        var expectedUsername = "joao.silva";

        final var updateUserDto = UpdateUserDto.builder()
                .cpf(expectedCpf)
                .email(expectedEmail)
                .phoneNumber(expectedPhoneNumber)
                .username(expectedUsername)
                .build();

        final var userDto = UserDto.builder()
                .uuid(expectedUuid)
                .cpf(expectedCpf)
                .email(expectedEmail)
                .phoneNumber(expectedPhoneNumber)
                .username(expectedUsername)
                .build();

        Mockito.when(userService.patchUser(Mockito.any(UUID.class), ArgumentMatchers.any(UpdateUserDto.class)))
                .thenReturn(userDto);

        //when
        final var request = MockMvcRequestBuilders.patch("/users/{uuid}", expectedUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDto));

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(expectedUuid.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf", Matchers.equalTo(expectedCpf)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.equalTo(expectedEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Matchers.equalTo(expectedPhoneNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo(expectedUsername)));

        Mockito.verify(userService, Mockito.times(1))
                .patchUser(ArgumentMatchers.eq(expectedUuid), ArgumentMatchers.eq(updateUserDto));

    }

    @Test
    public void givenAnInValidRequest_whenCallsPatchUser_shouldReturnConstraintViolationException() throws Exception {

        //given
        var expectedUuid = UUID.randomUUID();

        //when
        final var request = MockMvcRequestBuilders.patch("/users/{uuid}", expectedUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UpdateUserDto.builder().build()));

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_title", Matchers.equalTo("Parâmetros incorretos na requisição")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", Matchers.equalTo("Invalid update user request")));

        Mockito.verify(userService, Mockito.times(0))
                .patchUser(ArgumentMatchers.eq(expectedUuid), Mockito.any());
    }

    @Test
    public void givenAValidUuid_whenCallsDeleteUser_shouldReturnNoContent() throws Exception {

        //given
        var expectedUuid = UUID.randomUUID();

        Mockito.doNothing().when(userService).deleteUser(Mockito.any(UUID.class));

        //when
        final var request = MockMvcRequestBuilders.delete("/users/{uuid}", expectedUuid);

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(userService, Mockito.times(1))
                .deleteUser(ArgumentMatchers.eq(expectedUuid));

    }

    @Test
    public void givenAnInValidUuid_whenCallsDeleteUser_shouldReturnUserNotDeletedException() throws Exception {

        //given
        var expectedUuid = UUID.randomUUID();

        Mockito.doThrow(new UserNotDeletedException("Erro ao excluir usuário",
                        String.format("Não foi possível remover usuário pelo uuid: %s", expectedUuid)))
                .when(userService).deleteUser(Mockito.any(UUID.class));

        //when
        final var request = MockMvcRequestBuilders.delete("/users/{uuid}", expectedUuid);

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_title", Matchers.equalTo("Erro ao excluir usuário")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description", Matchers.equalTo(String.format("Não foi possível remover usuário pelo uuid: %s", expectedUuid))));

        Mockito.verify(userService, Mockito.times(1))
                .deleteUser(ArgumentMatchers.eq(expectedUuid));

    }

}
