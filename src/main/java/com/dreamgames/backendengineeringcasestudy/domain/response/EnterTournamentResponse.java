package com.dreamgames.backendengineeringcasestudy.domain.response;

import com.dreamgames.backendengineeringcasestudy.domain.dto.GroupRankDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record EnterTournamentResponse(
        String tournamentId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<GroupRankDTO> groupRanks
) {
}
