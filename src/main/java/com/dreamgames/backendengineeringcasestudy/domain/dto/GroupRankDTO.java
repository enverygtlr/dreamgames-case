package com.dreamgames.backendengineeringcasestudy.domain.dto;

import lombok.Builder;

@Builder
public record GroupRankDTO (
        String userId,
        Integer rank,
        String username,
        Integer score,
        String country
) {
}
