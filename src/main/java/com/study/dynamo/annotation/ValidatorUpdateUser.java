package com.study.dynamo.annotation;

import com.study.dynamo.dto.UpdateUserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class ValidatorUpdateUser implements ConstraintValidator<ValidUpdateUser, UpdateUserDto> {
    @Override
    public boolean isValid(UpdateUserDto updateUserDto, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Realizando validacao do updateUserDto: [{}]", updateUserDto);
        return !Objects.isNull(updateUserDto.username()) ||
                !Objects.isNull(updateUserDto.email()) ||
                !Objects.isNull(updateUserDto.phoneNumber()) ||
                !Objects.isNull(updateUserDto.cpf());
    }
}
