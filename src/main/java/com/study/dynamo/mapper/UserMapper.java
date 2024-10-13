package com.study.dynamo.mapper;

import com.study.dynamo.dto.CreateUserDto;
import com.study.dynamo.dto.UpdateUserDto;
import com.study.dynamo.dto.UserDto;
import com.study.dynamo.respository.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(UserEntity userEntity);

    @Mapping(source = "userId", target = "uuid")
    UserEntity toUserEntity(String userId, CreateUserDto createUserDto);

    @Mapping(source = "userId", target = "uuid")
    default void patchUserEntity(UserEntity userEntityToUpdate, UpdateUserDto updateUserDto) {
        if (Objects.nonNull(updateUserDto.cpf())) {
            userEntityToUpdate.setCpf(updateUserDto.cpf());
        }
        if (Objects.nonNull(updateUserDto.email())) {
            userEntityToUpdate.setEmail(updateUserDto.email());
        }
        if (Objects.nonNull(updateUserDto.phoneNumber())) {
            userEntityToUpdate.setPhoneNumber(updateUserDto.phoneNumber());
        }
        if (Objects.nonNull(updateUserDto.username())) {
            userEntityToUpdate.setUsername(updateUserDto.username());
        }
    }

}
