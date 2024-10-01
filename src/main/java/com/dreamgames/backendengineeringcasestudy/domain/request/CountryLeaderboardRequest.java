package com.dreamgames.backendengineeringcasestudy.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CountryLeaderboardRequest (
        @NotBlank  UUID tournamentId
) {
}