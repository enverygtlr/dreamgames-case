package com.dreamgames.backendengineeringcasestudy.domain.response;

public record UserResponse (
        String id,
        String username,
        String country,
        Integer level,
        Integer coin
) {
}