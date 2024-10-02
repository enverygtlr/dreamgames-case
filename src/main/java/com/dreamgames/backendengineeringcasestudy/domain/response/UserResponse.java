package com.dreamgames.backendengineeringcasestudy.domain.response;

import lombok.Builder;

@Builder
public record UserResponse (
        String id,
        String username,
        String country,
        Integer level,
        Integer coin
) {
}
