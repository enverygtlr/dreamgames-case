package com.dreamgames.backendengineeringcasestudy.mapper;

import com.dreamgames.backendengineeringcasestudy.domain.dto.GroupRankDTO;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Comparator;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ParticipantMapper {

    @Mapping(target = "userId", expression= "java(participant.getUser().getId().toString())")
    @Mapping(target = "username", source = "participant.user.username")
    @Mapping(target = "country", expression = "java(participant.getUser().getCountry().name())")  // Use custom expression
    GroupRankDTO toGroupRankDTO(Participant participant, int rank);
}
