package com.dreamgames.backendengineeringcasestudy.domain.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record GroupLeaderboardRequest (
        @NotBlank UUID userId,
        @NotBlank UUID tournamentId
) {
}