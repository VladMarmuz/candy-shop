package com.candyshop.mappers;

import com.candyshop.dto.UserUpdateRequest;
import com.candyshop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper extends Mappable<User, UserUpdateRequest> {
}
