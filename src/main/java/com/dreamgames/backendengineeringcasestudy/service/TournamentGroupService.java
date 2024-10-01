package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.dto.GroupRankDTO;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.mapper.ParticipantMapper;
import com.dreamgames.backendengineeringcasestudy.repository.ParticipantRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TournamentGroupService {
    private final TournamentGroupRepository tournamentGroupRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    @Transactional
    public TournamentGroup assignUserToTournamentGroup(Tournament tournament, User user) {
        TournamentGroup group = tournamentGroupRepository
                .findAvailableGroup(user.getCountry(), tournament)
                .orElseGet(() -> generateNewGroup(tournament));

        Participant newParticipant = generateNewParticipant(tournament, user, group);
        participantRepository.save(newParticipant);
        checkIfGroupIsReady(group);
        return group;
    }

    @Transactional
    public List<GroupRankDTO> getGroupRanks(TournamentGroup group) {
        var participants = participantRepository.findByGroup(group);
        return convertToGroupRankDTOs(participants);
    }

    private TournamentGroup generateNewGroup(Tournament tournament) {
        TournamentGroup group = TournamentGroup.builder()
                .tournament(tournament)
                .ready(false)
                .build();
        return tournamentGroupRepository.save(group);
    }

    private Participant generateNewParticipant(Tournament tournament, User user, TournamentGroup group) {
        return Participant.builder()
                .user(user)
                .score(0)
                .country(user.getCountry())
                .group(group)
                .tournament(tournament)
                .rewardClaimed(false)
                .build();
    }

    private List<GroupRankDTO> convertToGroupRankDTOs(List<Participant> participants) {
        List<Participant> sortedParticipants = participants.stream()
                .sorted(Comparator.comparing(Participant::getScore).reversed())
                .toList();

        return IntStream.range(0, sortedParticipants.size())
                .mapToObj(index -> {
                    Participant participant = sortedParticipants.get(index);
                    return participantMapper.toGroupRankDTO(participant, index + 1);
                })
                .collect(Collectors.toList());
    }

    private void checkIfGroupIsReady(TournamentGroup group) {
        if(participantRepository.findByGroup(group).size() == 5) {
            group.setReady(true);
            tournamentGroupRepository.save(group);
        }
    }

}
