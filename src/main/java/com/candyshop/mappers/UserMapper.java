package com.candyshop.mappers;

import com.candyshop.dto.UserDTO;
import com.candyshop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserDTO userDto);
    UserDTO toDto(User user);
}
