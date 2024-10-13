package com.study.dynamo.respository;

import com.study.dynamo.respository.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<UserEntity> findByUuid(UUID uuid);

    void save(UserEntity userEntity);

    UserEntity patch(UserEntity userEntity);

    void delete(UUID uuid);
}
