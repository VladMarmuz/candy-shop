package com.candyshop.mappers;

import com.candyshop.dto.auth.UserRegistrationRequest;
import com.candyshop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserRegistrationRequest userDto);
    UserRegistrationRequest toDto(User user);
}
