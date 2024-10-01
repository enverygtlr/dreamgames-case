package com.dreamgames.backendengineeringcasestudy.mapper;

import com.dreamgames.backendengineeringcasestudy.domain.dto.GroupRankDTO;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    @Mapping(target = "username", source = "participant.user.username")
    @Mapping(target = "country", expression = "java(participant.getUser().getCountry().name())")  // Use custom expression
    GroupRankDTO toGroupRankDTO(Participant participant, int rank);
}
