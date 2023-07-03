package com.candyshop.mappers;

import com.candyshop.dto.UserDto;
import com.candyshop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserDto userDto);
    UserDto toDto(User user);
}
