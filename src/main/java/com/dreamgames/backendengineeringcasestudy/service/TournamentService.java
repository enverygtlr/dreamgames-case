package com.dreamgames.backendengineeringcasestudy.service;


import com.dreamgames.backendengineeringcasestudy.domain.dto.GroupRankDTO;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.request.EnterTournamentRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.EnterTournamentResponse;
import com.dreamgames.backendengineeringcasestudy.exception.MissingRequirementException;
import com.dreamgames.backendengineeringcasestudy.exception.TournamentNotFoundException;
import com.dreamgames.backendengineeringcasestudy.exception.UserAlreadyInTournamentException;
import com.dreamgames.backendengineeringcasestudy.exception.UserHasUnclaimedRewardException;
import com.dreamgames.backendengineeringcasestudy.mapper.ParticipantMapper;
import com.dreamgames.backendengineeringcasestudy.repository.CountryScoreRepository;
import com.dreamgames.backendengineeringcasestudy.repository.ParticipantRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentGroupRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;
    private final CountryScoreRepository countryScoreRepository;
    private final UserService userService;
    private final TournamentGroupRepository tournamentGroupRepository;
    private final TournamentGroupService tournamentGroupService;


    @Transactional
    public EnterTournamentResponse enterTournament(EnterTournamentRequest request) {
        User user = userService.getUserById(request.userId());
        Tournament activeTournament = tournamentRepository.findFirstByIsActiveTrue().orElseThrow(TournamentNotFoundException::new);
        validateUserEligibleForTournament(user, activeTournament);
        validateUserRequirements(user);

        var tournamentGroup = tournamentGroupService.assignUserToTournamentGroup(activeTournament, user);
        var groupRankDTOs = tournamentGroupService.getGroupRanks(tournamentGroup);

        return  EnterTournamentResponse.builder()
                .tournamentId(activeTournament.getId().toString())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .groupRanks(groupRankDTOs)
                .build();
    }

    private void startNewTournament() {

    }

    private void endCurrentTournament() {

    }

    private void validateUserRequirements(User user) {
        if (!(user.getCoin() >= 1000 && user.getLevel() >= 20)) {
            throw new MissingRequirementException();
        }
    }

    private void validateUserEligibleForTournament(User user, Tournament tournament) {
        participantRepository.findByUserAndTournament(user, tournament).ifPresent(participant->{throw new UserAlreadyInTournamentException();});

        if (participantRepository.existsByUserAndRewardClaimedFalse(user)) {
           throw new UserHasUnclaimedRewardException();
        }
    }
}
