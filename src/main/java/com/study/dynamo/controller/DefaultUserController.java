package com.study.dynamo.controller;

import com.study.dynamo.dto.CreateUserDto;
import com.study.dynamo.dto.UpdateUserDto;
import com.study.dynamo.dto.UserDto;
import com.study.dynamo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DefaultUserController implements UserController {

    private final UserService userService;

    public ResponseEntity<UserDto> getOneUser(UUID uuid) {
        return ResponseEntity
                .ok(userService.findUserByUuid(uuid));
    }

    public ResponseEntity<UserDto> createUser(CreateUserDto createUserDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(createUserDto));
    }

    public ResponseEntity<UserDto> patchUser(UUID uuid, UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.patchUser(uuid, updateUserDto));

    }

    public void deleteUser(UUID uuid) {
        userService.deleteUser(uuid);
    }

}
