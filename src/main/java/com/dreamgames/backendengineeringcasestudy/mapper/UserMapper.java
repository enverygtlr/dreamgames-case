package com.dreamgames.backendengineeringcasestudy.mapper;

import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.response.ClaimRewardResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserUpdateLevelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserResponse convertToUserResponse(User user);

    UserUpdateLevelResponse convertToUserUpdateLevelResponse(User user);

    ClaimRewardResponse convertToClaimRewardResponse(User user);
}

