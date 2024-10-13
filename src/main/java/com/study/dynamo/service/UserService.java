package com.study.dynamo.service;

import com.study.dynamo.dto.CreateUserDto;
import com.study.dynamo.dto.UpdateUserDto;
import com.study.dynamo.dto.UserDto;

import java.util.UUID;

public interface UserService {

    UserDto findUserByUuid(UUID uuid);

    UserDto createUser(CreateUserDto createUserDto);

    UserDto patchUser(UUID uuid, UpdateUserDto updateUserDto);

    void deleteUser(UUID uuid);
}
