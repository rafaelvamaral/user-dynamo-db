package com.study.dynamo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.dynamo.dto.CreateUserDto;
import com.study.dynamo.dto.UpdateUserDto;
import com.study.dynamo.respository.UserRepository;
import com.study.dynamo.respository.entity.UserEntity;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@IntegrationTest
public class UserE2ETest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void givenAValidUuid_whenCallsGetUser_shouldReturnUser() throws Exception {

        //given
        var expectedUuid = UUID.randomUUID();
        var expectedCpf = "123456789";
        var expectedEmail = "joao@silva.com";
        var expectedPhoneNumber = "119878674768";
        var expectedUsername = "joao.silva";

        createUser(expectedUuid, expectedCpf, expectedEmail, expectedPhoneNumber, expectedUsername);

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

    }

    @Test
    public void givenAnInvalidUuid_whenCallsGetUser_shouldReturnNotFoundException() throws Exception {

        //given
        final var expectedUuid = UUID.randomUUID();

        //when
        final var request = MockMvcRequestBuilders.get("/users/{uuid}", expectedUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_title", Matchers.equalTo("Não encontrado")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description", Matchers.equalTo(String.format("Usuário não encontrado pelo uuid: %s", expectedUuid))));

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf", Matchers.equalTo(expectedCpf)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.equalTo(expectedEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Matchers.equalTo(expectedPhoneNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo(expectedUsername)));

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


        createUser(expectedUuid, expectedCpf, expectedEmail, expectedPhoneNumber, expectedUsername);

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
    }

    @Test
    public void givenAValidUuid_whenCallsDeleteUser_shouldReturnNoContent() throws Exception {

        //given
        var expectedUuid = UUID.randomUUID();

        //when
        final var request = MockMvcRequestBuilders.delete("/users/{uuid}", expectedUuid);

        final var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

    }


    private void createUser(UUID expectedUuid, String expectedCpf, String expectedEmail, String expectedPhoneNumber, String expectedUsername) {
        var userEntity = UserEntity.builder()
                .uuid(expectedUuid)
                .cpf(expectedCpf)
                .email(expectedEmail)
                .phoneNumber(expectedPhoneNumber)
                .username(expectedUsername)
                .build();

        userRepository.save(userEntity);
    }
}




