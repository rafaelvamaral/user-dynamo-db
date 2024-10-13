package com.study.dynamo.respository;

import com.study.dynamo.respository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
@Slf4j
public class DefaultUserRepository implements UserRepository {

    private static final String TABLE_NANE = "user_table";

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private DynamoDbTable<UserEntity> dynamoDbTable;

    @Override
    public Optional<UserEntity> findByUuid(UUID uuid) {
        log.info("Recuperando usuario pelo uuid [{}]", uuid);
        var table = getTable();
        var searchKey = buildKey(uuid);
        var result = table.getItem(searchKey);
        return Optional.ofNullable(result);
    }

    @Override
    public void save(UserEntity userEntity) {
        log.info("Salvando usuario com uuid [{}] e username [{}]", userEntity.getUuid(), userEntity.getUsername());
        var table = getTable();
        table.putItem(userEntity);
    }

    @Override
    public UserEntity patch(UserEntity userEntity) {
        log.info("Atualizando usuario com uuid [{}]", userEntity.getUuid());
        var table = getTable();
        return table.updateItem(userEntity);
    }

    @Override
    public void delete(UUID uuid) {
        log.info("Removendo usuario com uuid [{}]", uuid);
        var table = getTable();
        var searchKey = buildKey(uuid);
        table.deleteItem(searchKey);
    }

    private static Key buildKey(UUID uuid) {
        return Key.builder().partitionValue(uuid.toString()).build();
    }

    public DynamoDbTable<UserEntity> getTable() {
        if (dynamoDbTable == null) {
            dynamoDbTable = dynamoDbEnhancedClient.table(TABLE_NANE, TableSchema.fromBean(UserEntity.class));
        }
        return dynamoDbTable;
    }
}
