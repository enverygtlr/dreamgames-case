package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.constants.ApplicationConstants;
import com.dreamgames.backendengineeringcasestudy.domain.dto.GroupRankDTO;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.domain.request.ClaimRewardRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.CountryLeaderboardRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.EnterTournamentRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.GroupLeaderboardRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.ClaimRewardResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.CountryLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.EnterTournamentResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.GroupLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.event.UserUpdateLevelEvent;
import com.dreamgames.backendengineeringcasestudy.exception.*;
import com.dreamgames.backendengineeringcasestudy.mapper.UserMapper;
import com.dreamgames.backendengineeringcasestudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService implements ApplicationListener<UserUpdateLevelEvent> {
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TournamentGroupService tournamentGroupService;
    private final UserRepository userRepository;
    private final CountryScoreService countryScoreService;


    @Transactional
    public EnterTournamentResponse enterTournament(EnterTournamentRequest request) {
        User user = userService.getUserById(request.userId());
        Tournament activeTournament = tournamentRepository.findFirstByIsActiveTrue().orElseThrow(TournamentNotFoundException::new);
        validateUserEligibleForTournament(user, activeTournament);
        validateUserRequirements(user);
        processTournamentEnter(user);
        var tournamentGroup = tournamentGroupService.assignUserToTournamentGroup(activeTournament, user);
        List<GroupRankDTO> groupRankings = tournamentGroupService.getRankings(tournamentGroup);

        return  EnterTournamentResponse.builder()
                .tournamentId(activeTournament.getId().toString())
                .startTime(activeTournament.getStartTime())
                .endTime(activeTournament.getEndTime())
                .groupRanks(groupRankings)
                .build();
    }

    public GroupLeaderboardResponse getGroupLeaderboard(GroupLeaderboardRequest request) {
        User user = userService.getUserById(request.userId());
        Tournament tournament = tournamentRepository.findById(request.tournamentId()).orElseThrow(TournamentNotFoundException::new);
        TournamentGroup group = tournamentGroupService.getGroupOfUser(tournament, user).orElseThrow(TournamentNotFoundException::new);
        var groupRanks = tournamentGroupService.getRankings(group);
        int rank = tournamentGroupService.getRankingOfUser(group, user);

        return GroupLeaderboardResponse.builder()
                .username(user.getUsername())
                .rank(rank)
                .tournamentId(tournament.getId().toString())
                .startTime(tournament.getStartTime())
                .endTime(tournament.getEndTime())
                .groupRanks(groupRanks)
                .build();
    }

    public CountryLeaderboardResponse getCountryLeaderboard(CountryLeaderboardRequest request) {
        Tournament tournament = tournamentRepository.findById(request.tournamentId()).orElseThrow(TournamentNotFoundException::new);
        var countryScoreRanks = countryScoreService.getCountryScoreLeaderboard(tournament);

        return CountryLeaderboardResponse.builder()
                .tournamentId(tournament.getId().toString())
                .startTime(tournament.getStartTime())
                .endTime(tournament.getEndTime())
                .countryScoreRanks(countryScoreRanks)
                .build();
    }

    @Transactional
    public ClaimRewardResponse claimReward(ClaimRewardRequest request) {
        User user = userService.getUserById(request.userId());
        Participant participant = participantRepository.findParticipantWithReward(user)
                .orElseThrow(NoRewardAvailableException::new);

        int rank = tournamentGroupService.getRankingOfUser(participant.getGroup(), user);
        User updatedUser = processReward(user, rank);
        participant.setHasReward(false);
        participantRepository.save(participant);
        return userMapper.convertToClaimRewardResponse(updatedUser);
    }

    private void processTournamentEnter(User user) {
        user.setCoin(user.getCoin() - ApplicationConstants.TOURNAMENT_FEE);
        userRepository.save(user);
    }

    private User processReward(User user, int rank) {
        if (rank == 1) {
            user.setCoin(user.getCoin() + ApplicationConstants.FIRST_REWARD);
            return userRepository.save(user);
        } else if (rank == 2) {
            user.setCoin(user.getCoin() + ApplicationConstants.SECOND_REWARD);
            return userRepository.save(user);
        }
        return user;
    }

    private void validateUserRequirements(User user) {
        if (!(user.getCoin() >= ApplicationConstants.TOURNAMENT_FEE && user.getLevel() >= ApplicationConstants.TOURNAMENT_MINLEVEL)) {
            throw new MissingRequirementException();
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void startTournament() {
        if(tournamentRepository.existsByIsActiveTrue()) return;
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.MIDNIGHT);
        LocalDateTime endTime = startTime.withHour(20);

        var tournament = Tournament.builder()
                .startTime(startTime)
                .endTime(endTime)
                .isActive(true)
                .build();

        tournamentRepository.save(tournament);
        countryScoreService.initialiseCountryScores(tournament);
    }

    @Transactional
    @Scheduled(cron = "0 0 20 * * *", zone = "UTC")
    public void endTournament() {
        tournamentRepository.findFirstByIsActiveTrue().ifPresent(tournament -> {
            tournament.setIsActive(false);
            tournamentRepository.save(tournament);
            dispatchRewards(tournament);
        });
    }

    private void dispatchRewards(Tournament tournament) {
        tournamentGroupService.dispatchTournamentRewards(tournament);
    }

    private void validateUserEligibleForTournament(User user, Tournament tournament) {
        participantRepository.findByUserAndTournament(user, tournament).ifPresent(participant->{throw new UserAlreadyInTournamentException();});

        if (participantRepository.existsUserWithUnclaimedReward(user)) {
           throw new UserHasUnclaimedRewardException();
        }
    }

    private void updateCountryScore(Tournament tournament, Country country, int score) {
        countryScoreService.addCountryScore(tournament,country,score);
    }

    private void updateParticipantScore(User user, int score) {
        tournamentRepository.findFirstByIsActiveTrue()
                .flatMap(activeTournament -> participantRepository.findByUserAndTournament(user, activeTournament))
                .filter(participant -> participant.getGroup().getReady())
                .ifPresent(participant -> {
                    participant.setScore(participant.getScore() + score);
                    participantRepository.save(participant);
                    updateCountryScore(participant.getTournament(), participant.getCountry(), score);
                });
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserUpdateLevelEvent event) {
        updateParticipantScore(event.getUser(), event.getScore());
    }
}
