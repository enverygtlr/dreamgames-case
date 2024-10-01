package com.dreamgames.backendengineeringcasestudy.domain.dto;

import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;

public record ParticipantRank (
        int rank,
        Participant participant
){
}
