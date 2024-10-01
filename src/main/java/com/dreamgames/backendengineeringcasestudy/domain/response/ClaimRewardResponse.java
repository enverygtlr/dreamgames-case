package com.dreamgames.backendengineeringcasestudy.domain.response;

public record ClaimRewardResponse (
        String id,
        String username,
        String country,
        Integer level,
        Integer coin
) {
}
