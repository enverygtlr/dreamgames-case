package com.dreamgames.backendengineeringcasestudy.domain.dto;

import lombok.Builder;

@Builder
public record GroupRankDTO (
        Integer rank,
        String username,
        Integer score,
        String country
) {
}
