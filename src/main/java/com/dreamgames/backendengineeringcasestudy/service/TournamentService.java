package com.dreamgames.backendengineeringcasestudy.service;


import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.request.ClaimRewardRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.EnterTournamentRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.GroupLeaderboardRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.ClaimRewardResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.EnterTournamentResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.GroupLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.event.UserUpdateLevelEvent;
import com.dreamgames.backendengineeringcasestudy.exception.*;
import com.dreamgames.backendengineeringcasestudy.mapper.UserMapper;
import com.dreamgames.backendengineeringcasestudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentService implements ApplicationListener<UserUpdateLevelEvent> {
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;
    private final CountryScoreRepository countryScoreRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TournamentGroupRepository tournamentGroupRepository;
    private final TournamentGroupService tournamentGroupService;
    private final UserRepository userRepository;


    @Transactional
    public EnterTournamentResponse enterTournament(EnterTournamentRequest request) {
        User user = userService.getUserById(request.userId());
        Tournament activeTournament = tournamentRepository.findFirstByIsActiveTrue().orElseThrow(TournamentNotFoundException::new);
        validateUserEligibleForTournament(user, activeTournament);
        validateUserRequirements(user);

        var tournamentGroup = tournamentGroupService.assignUserToTournamentGroup(activeTournament, user);
        var groupRankDTOs = tournamentGroupService.getGroupRankDTOs(tournamentGroup);

        return  EnterTournamentResponse.builder()
                .tournamentId(activeTournament.getId().toString())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .groupRanks(groupRankDTOs)
                .build();
    }

    public GroupLeaderboardResponse getGroupLeaderboard(GroupLeaderboardRequest request) {
        User user = userService.getUserById(request.userId());
        Tournament tournament = tournamentRepository.findById(request.tournamentId()).orElseThrow(TournamentNotFoundException::new);
        TournamentGroup group = tournamentGroupService.getGroupOfUser(tournament, user).orElseThrow(TournamentNotFoundException::new);
        var groupRanks = tournamentGroupService.getGroupRankDTOs(group);
        int rank = tournamentGroupService.getRankingOfUser(group, user);

        return GroupLeaderboardResponse.builder()
                .username(user.getUsername())
                .rank(rank)
                .tournamentId(tournament.getId().toString())
                .startTime(tournament.getStartTime())
                .endTime(tournament.getEnd_time())
                .groupRanks(groupRanks)
                .build();
    }

    @Transactional
    public ClaimRewardResponse claimReward(ClaimRewardRequest request) {
        User user = userService.getUserById(request.userId());

        Optional<Participant> participantOpt = participantRepository.findFirstByUserAndRewardClaimedFalseAndTournamentIsActiveFalse(user);

        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            int rank = tournamentGroupService.getRankingOfUser(participant.getGroup(), user);
            User updatedUser = processReward(user, rank);
            participant.setRewardClaimed(true);
            participantRepository.save(participant);
            return userMapper.convertToClaimRewardResponse(updatedUser);
        } else {
            throw new NoRewardAvailableException();
        }
    }

    private User processReward(User user, int rank) {
        if (rank == 1) {
            user.setCoin(user.getCoin() + 10000);
            return userRepository.save(user);
        } else if (rank == 2) {
            user.setCoin(user.getCoin() + 5000);
            return userRepository.save(user);
        }
        return user;
    }

    private void validateUserRequirements(User user) {
        if (!(user.getCoin() >= 1000 && user.getLevel() >= 20)) {
            throw new MissingRequirementException();
        }
    }

    private void startNewTournament() {

    }

    private void endCurrentTournament() {

    }


    private void validateUserEligibleForTournament(User user, Tournament tournament) {
        participantRepository.findByUserAndTournament(user, tournament).ifPresent(participant->{throw new UserAlreadyInTournamentException();});

        if (participantRepository.existsByUserAndRewardClaimedFalse(user)) {
           throw new UserHasUnclaimedRewardException();
        }
    }

    private void updateParticipantScore(User user) {
        tournamentRepository.findFirstByIsActiveTrue()
                .flatMap(activeTournament -> participantRepository.findByUserAndTournament(user, activeTournament))
                .filter(participant -> participant.getGroup().getReady())
                .ifPresent(participant -> {
                    participant.setScore(participant.getScore() + 1);
                    participantRepository.save(participant);
                });
    }

    @Override
    public void onApplicationEvent(UserUpdateLevelEvent event) {
        updateParticipantScore(event.getUser());
    }
}
