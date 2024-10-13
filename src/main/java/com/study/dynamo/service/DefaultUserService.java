package com.study.dynamo.service;

import com.study.dynamo.dto.CreateUserDto;
import com.study.dynamo.dto.UpdateUserDto;
import com.study.dynamo.dto.UserDto;
import com.study.dynamo.exception.UserNotCreatedException;
import com.study.dynamo.exception.UserNotDeletedException;
import com.study.dynamo.exception.UserNotFoundException;
import com.study.dynamo.exception.UserNotPatchedException;
import com.study.dynamo.mapper.UserMapper;
import com.study.dynamo.respository.UserRepository;
import com.study.dynamo.respository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultUserService implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public UserDto patchUser(UUID uuid, UpdateUserDto updateUserDto) {
        var userEntityToUpdate = findUser(uuid);
        try {
            userMapper.patchUserEntity(userEntityToUpdate, updateUserDto);
            var userEntityUpdated = userRepository.patch(userEntityToUpdate);
            return userMapper.toUserDto(userEntityUpdated);
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário com uuid: [{}]", uuid, e);
            throw new UserNotPatchedException("Erro ao atualizar usuário",
                    String.format("Não foi possível atualizar usuário com uuid: %s", uuid));
        }
    }

    @Override
    public UserDto findUserByUuid(UUID uuid) {
        var userEntity = findUser(uuid);
        return userMapper.toUserDto(userEntity);
    }

    private UserEntity findUser(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> {
                    var exception = new UserNotFoundException("Não encontrado",
                            String.format("Usuário não encontrado pelo uuid: %s", uuid));
                    log.error("Erro ao atualizar usuário com uuid: [{}]", uuid, exception);
                    return exception;
                });
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        try {
            var userId = UUID.randomUUID().toString();
            var userEntity = userMapper.toUserEntity(userId, createUserDto);
            userRepository.save(userEntity);
            return userMapper.toUserDto(userEntity);
        } catch (Exception e) {
            log.error("Erro ao criar usuário com username: [{}]", createUserDto.username(), e);
            throw new UserNotCreatedException("Erro ao criar usuário",
                    String.format("Não foi possível criar usuário com username: %s", createUserDto.username()));
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        try {
            userRepository.delete(uuid);
        } catch (Exception e) {
            log.error("Erro ao remover usuário com uuid: [{}]", uuid, e);
            throw new UserNotDeletedException("Erro ao excluir usuário",
                    String.format("Não foi possível remover usuário pelo uuid: %s", uuid));
        }
    }
}
