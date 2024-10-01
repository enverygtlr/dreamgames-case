package com.dreamgames.backendengineeringcasestudy.domain.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ClaimRewardRequest (
        @NotBlank UUID userId
) {
}