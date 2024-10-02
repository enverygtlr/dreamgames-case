package com.dreamgames.backendengineeringcasestudy.domain.dto;

import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import lombok.Builder;

@Builder
public record ParticipantRank (
        int rank,
        Participant participant
){
}
