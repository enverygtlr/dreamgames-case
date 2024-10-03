package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.dto.GroupRankDTO;
import com.dreamgames.backendengineeringcasestudy.domain.dto.ParticipantRank;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<GroupRankDTO> getRankings(TournamentGroup group) {
        var participantRanks = getParticipantRanks(group);
        return convertToGroupRankDTOs(participantRanks);
    }

    public int getRankingOfUser(TournamentGroup group, User user) {
        List<ParticipantRank> participantRanks = getParticipantRanks(group);

        return participantRanks.stream()
                .filter(participantRank -> participantRank.participant().getUser().equals(user))
                .map(ParticipantRank::rank)
                .findFirst()
                .orElse(-1);
    }

    public Optional<TournamentGroup> getGroupOfUser(Tournament tournament, User user) {
        return participantRepository.findByUserAndTournament(user, tournament)
                .map(Participant::getGroup);
    }

    @Transactional
    public void dispatchTournamentRewards(Tournament tournament) {
        tournamentGroupRepository.findAllByTournament(tournament).forEach(group ->
                getParticipantRanks(group).stream()
                        .filter(participantRank -> participantRank.rank() == 1 || participantRank.rank() == 2)
                        .map(ParticipantRank::participant)
                        .forEach(participant -> {
                            participant.setHasReward(true);
                            participantRepository.save(participant);
                        })
        );
    }

    private List<ParticipantRank> getParticipantRanks(TournamentGroup group) {
        var participants = participantRepository.findByGroup(group);

        List<Participant> sortedParticipants = participants.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                .toList();

        List<ParticipantRank> participantRanks = new ArrayList<>();
        int rank = 1;
        for (int i = 0; i < sortedParticipants.size(); i++) {
            if (i > 0 && !sortedParticipants.get(i).getScore().equals(sortedParticipants.get(i - 1).getScore())) {
                rank = rank + 1;
            }
            participantRanks.add(new ParticipantRank(rank, sortedParticipants.get(i)));
        }
        return participantRanks;
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
                .hasReward(false)
                .build();
    }

    private List<GroupRankDTO> convertToGroupRankDTOs(List<ParticipantRank> participantRanks) {
            return participantRanks.stream().map(rank -> {
                        return participantMapper.toGroupRankDTO(rank.participant(), rank.rank());
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
