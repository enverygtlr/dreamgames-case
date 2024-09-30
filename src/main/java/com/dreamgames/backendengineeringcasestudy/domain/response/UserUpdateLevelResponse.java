package com.dreamgames.backendengineeringcasestudy.domain.response;

public record UserUpdateLevelResponse (
        String id,
        String username,
        String country,
        Integer level,
        Integer coin
) {
}
