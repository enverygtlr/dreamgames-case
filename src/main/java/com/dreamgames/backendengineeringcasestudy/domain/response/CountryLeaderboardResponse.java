package com.dreamgames.backendengineeringcasestudy.domain.response;

import com.dreamgames.backendengineeringcasestudy.domain.dto.CountryScoreRank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CountryLeaderboardResponse (
        String tournamentId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<CountryScoreRank> countryScoreRanks
) {
}
