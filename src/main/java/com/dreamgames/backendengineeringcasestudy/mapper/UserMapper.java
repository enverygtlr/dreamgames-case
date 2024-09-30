package com.dreamgames.backendengineeringcasestudy.mapper;

import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserResponse convertToUserResponse(User user);
}

