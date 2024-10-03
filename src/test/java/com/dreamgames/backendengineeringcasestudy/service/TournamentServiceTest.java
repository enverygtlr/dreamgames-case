package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.dto.CountryScoreRank;
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
import com.dreamgames.backendengineeringcasestudy.exception.MissingRequirementException;
import com.dreamgames.backendengineeringcasestudy.exception.NoRewardAvailableException;
import com.dreamgames.backendengineeringcasestudy.exception.TournamentNotFoundException;
import com.dreamgames.backendengineeringcasestudy.exception.UserHasUnclaimedRewardException;
import com.dreamgames.backendengineeringcasestudy.mapper.UserMapper;
import com.dreamgames.backendengineeringcasestudy.repository.ParticipantRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {
    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TournamentGroupService tournamentGroupService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryScoreService countryScoreService;

    @InjectMocks
    private TournamentService tournamentService;


    @Test
    void enterTournament_shouldSucceed() {
        // Given
        var userId = UUID.randomUUID();
        var request = new EnterTournamentRequest(userId);

        var user = User.builder()
                .id(userId)
                .coin(10000)
                .level(35)
                .build();

        var tournamentId = UUID.randomUUID();
        var startTime = LocalDateTime.parse("2023-10-24T00:00:00");
        var endTime = LocalDateTime.parse("2023-10-24T20:00:00");

        var tournament = Tournament.builder()
                .id(tournamentId)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        var groupRanking = List.of(Mockito.mock(GroupRankDTO.class));

        var expected = EnterTournamentResponse.builder()
                .tournamentId(tournamentId.toString())
                .startTime(startTime)
                .endTime(endTime)
                .groupRanks(groupRanking)
                .build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(tournamentRepository.findFirstByIsActiveTrue()).thenReturn(Optional.of(tournament));
        when(participantRepository.findByUserAndTournament(user, tournament)).thenReturn(Optional.empty());
        when(participantRepository.existsUserWithUnclaimedReward(user)).thenReturn(false);
        when(tournamentGroupService.assignUserToTournamentGroup(tournament, user)).thenReturn(Mockito.mock(TournamentGroup.class));
        when(tournamentGroupService.getRankings(any())).thenReturn(groupRanking);

        // When
        var actual = tournamentService.enterTournament(request);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void enterTournament_shouldThrowExceptionWhenNoActiveTournamentIsFound() {
        // Given
        var userId = UUID.randomUUID();
        var request = new EnterTournamentRequest(userId);

        when(userService.getUserById(userId)).thenReturn(Mockito.mock(User.class));
        when(tournamentRepository.findFirstByIsActiveTrue()).thenReturn(Optional.empty());

        // When - Then
        assertThrows(TournamentNotFoundException.class, () -> tournamentService.enterTournament(request));

        verify(userService, times(1)).getUserById(userId);
        verify(tournamentRepository, times(1)).findFirstByIsActiveTrue();
    }

    @Test
    void enterTournament_shouldThrowExceptionWhenUserHasUnclaimedReward() {
        // Given
        var userId = UUID.randomUUID();
        var request = new EnterTournamentRequest(userId);

        var user = User.builder()
                .id(userId)
                .coin(10000)
                .level(35)
                .build();

        var tournament = Mockito.mock(Tournament.class);

        when(userService.getUserById(userId)).thenReturn(user);
        when(tournamentRepository.findFirstByIsActiveTrue()).thenReturn(Optional.of(tournament));
        when(participantRepository.existsUserWithUnclaimedReward(user)).thenReturn(true);
        when(participantRepository.findByUserAndTournament(user, tournament)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(UserHasUnclaimedRewardException.class, () -> tournamentService.enterTournament(request));

        verify(userService, times(1)).getUserById(userId);
        verify(tournamentRepository, times(1)).findFirstByIsActiveTrue();
        verify(participantRepository, times(1)).existsUserWithUnclaimedReward(user);
        verify(participantRepository, times(1)).findByUserAndTournament(user, tournament);
    }

    @Test
    void enterTournament_shouldThrowExceptionWhenUserHasMissingLevels() {
        // Given
        var userId = UUID.randomUUID();
        var request = new EnterTournamentRequest(userId);
        var tournament = Mockito.mock(Tournament.class);

        var userWithMissingLevel = User.builder()
                .id(userId)
                .coin(10000)
                .level(5)
                .build();


        when(userService.getUserById(userId)).thenReturn(userWithMissingLevel);
        when(tournamentRepository.findFirstByIsActiveTrue()).thenReturn(Optional.of(tournament));
        when(participantRepository.findByUserAndTournament(userWithMissingLevel, tournament)).thenReturn(Optional.empty());
        when(participantRepository.existsUserWithUnclaimedReward(userWithMissingLevel)).thenReturn(false);

        // When - Then
        assertThrows(MissingRequirementException.class, () -> tournamentService.enterTournament(request));

        verify(userService, times(1)).getUserById(userId);
        verify(tournamentRepository, times(1)).findFirstByIsActiveTrue();
        verify(participantRepository, times(1)).existsUserWithUnclaimedReward(userWithMissingLevel);
        verify(participantRepository, times(1)).findByUserAndTournament(userWithMissingLevel, tournament);


    }

    @Test
    void enterTournament_shouldThrowExceptionWhenUserHasMissingCoins() {
        //Given
        var userId = UUID.randomUUID();
        var request = new EnterTournamentRequest(userId);
        var tournament = Mockito.mock(Tournament.class);
        var userWithMissingCoins = User.builder()
                .id(userId)
                .coin(10)
                .level(45)
                .build();

        when(userService.getUserById(userId)).thenReturn(userWithMissingCoins);
        when(tournamentRepository.findFirstByIsActiveTrue()).thenReturn(Optional.of(tournament));
        when(participantRepository.findByUserAndTournament(userWithMissingCoins, tournament)).thenReturn(Optional.empty());
        when(participantRepository.existsUserWithUnclaimedReward(userWithMissingCoins)).thenReturn(false);

        // When - Then
        assertThrows(MissingRequirementException.class, () -> tournamentService.enterTournament(request));

        verify(userService, times(1)).getUserById(userId);
        verify(tournamentRepository, times(1)).findFirstByIsActiveTrue();
        verify(participantRepository, times(1)).existsUserWithUnclaimedReward(userWithMissingCoins);
        verify(participantRepository, times(1)).findByUserAndTournament(userWithMissingCoins, tournament);
    }

    @Test
    void getGroupLeaderboard_shouldSucceed() {
        // Given
        var username = "ahmet";
        var userId = UUID.randomUUID();
        var user = User.builder()
                .username(username)
                .id(userId)
                .coin(10000)
                .level(35)
                .build();

        var tournamentId = UUID.randomUUID();
        var startTime = LocalDateTime.parse("2023-10-24T00:00:00");
        var endTime = LocalDateTime.parse("2023-10-24T20:00:00");

        var tournament = Tournament.builder()
                .id(tournamentId)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        var tournamentGroup = TournamentGroup.builder().build();

        var request = new GroupLeaderboardRequest(userId, tournamentId);

        var groupRanking = List.of(Mockito.mock(GroupRankDTO.class));

        int rank = 1;

        when(userService.getUserById(userId)).thenReturn(user);
        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(tournamentGroupService.getGroupOfUser(tournament, user)).thenReturn(Optional.of(tournamentGroup));
        when(tournamentGroupService.getRankings(tournamentGroup)).thenReturn(groupRanking);
        when(tournamentGroupService.getRankingOfUser(tournamentGroup, user)).thenReturn(rank);

        var expected = GroupLeaderboardResponse.builder()
                .username(username)
                .rank(rank)
                .tournamentId(tournamentId.toString())
                .startTime(startTime)
                .endTime(endTime)
                .groupRanks(groupRanking)
                .build();

        // When
        var actual = tournamentService.getGroupLeaderboard(request);

        // Then
        assertEquals(expected, actual);

        verify(userService, times(1)).getUserById(userId);
        verify(tournamentRepository, times(1)).findById(tournamentId);
        verify(tournamentGroupService, times(1)).getGroupOfUser(tournament, user);
        verify(tournamentGroupService, times(1)).getRankings(tournamentGroup);
        verify(tournamentGroupService, times(1)).getRankingOfUser(tournamentGroup, user);
    }

    @Test
    void getGroupLeaderboard_shouldThrowExceptionWhenTournamentNotFound() {
        // Given
        var userId = UUID.randomUUID();
        var tournamentId = UUID.randomUUID();

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

        var request = new GroupLeaderboardRequest(userId, tournamentId);

        //When - Then
        assertThrows(TournamentNotFoundException.class, () -> tournamentService.getGroupLeaderboard(request));

        verify(userService, times(1)).getUserById(userId);
        verify(tournamentRepository, times(1)).findById(tournamentId);
    }

    @Test
    void getCountryLeaderboard_shouldSucceed() {
        // Given
        var tournamentId = UUID.randomUUID();
        var startTime = LocalDateTime.parse("2023-10-24T00:00:00");
        var endTime = LocalDateTime.parse("2023-10-24T20:00:00");

        var tournament = Tournament.builder()
                .id(tournamentId)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        var countryScoreRanks = List.of(Mockito.mock(CountryScoreRank.class));

        var request = new CountryLeaderboardRequest(tournamentId);

        var expected = CountryLeaderboardResponse.builder()
                .tournamentId(tournamentId.toString())
                .startTime(startTime)
                .endTime(endTime)
                .countryScoreRanks(countryScoreRanks)
                .build();

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(countryScoreService.getCountryScoreLeaderboard(tournament)).thenReturn(countryScoreRanks);

        // When
        var actual = tournamentService.getCountryLeaderboard(request);

        //Then
        assertEquals(actual, expected);

        verify(tournamentRepository, times(1)).findById(tournamentId);
        verify(countryScoreService, times(1)).getCountryScoreLeaderboard(tournament);
    }

    @Test
    void getCountryLeaderboard_shouldThrowExceptionWhenTournamentNotFound() {
        // Given
        var tournamentId = UUID.randomUUID();
        var request = new CountryLeaderboardRequest(tournamentId);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(TournamentNotFoundException.class, () -> tournamentService.getCountryLeaderboard(request));

        verify(tournamentRepository, times(1)).findById(tournamentId);
    }

    @Test
    void claimReward_shouldSucceed() {
        // Given
        var username = "ahmet";
        var userId = UUID.randomUUID();
        var user = User.builder()
                .username(username)
                .country(Country.TR)
                .id(userId)
                .coin(10000)
                .level(35)
                .build();

        int rank = 3;

        var participant = Mockito.mock(Participant.class);

        var request = new ClaimRewardRequest(userId);

        var expected = ClaimRewardResponse.builder()
                .id(userId.toString())
                .username(username)
                .country(user.getCountry().name())
                .level(user.getLevel())
                .coin(user.getCoin())
                .build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(participantRepository.findParticipantWithReward(user)).thenReturn(Optional.of(participant));
        when(tournamentGroupService.getRankingOfUser(any(), eq(user))).thenReturn(rank);
        when(userMapper.convertToClaimRewardResponse(user)).thenReturn(expected);

        // When
        var actual = tournamentService.claimReward(request);

        //Then
        assertEquals(actual, expected);

        verify(userService, times(1)).getUserById(userId);
        verify(participantRepository, times(1)).findParticipantWithReward(user);
        verify(tournamentGroupService, times(1)).getRankingOfUser(any(), eq(user));
        verify(tournamentGroupService, times(1)).getRankingOfUser(any(), eq(user));
        verify(participant, times(1)).setHasReward(true);
        verify(participantRepository, times(1)).save(any());
        verify(userMapper, times(1)).convertToClaimRewardResponse(user);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1000, 11000",
            "2, 1000, 6000",
            "3, 1000, 1000"
    })
    void claimReward_shouldUpdateUserCoinsForRanking(int rank, int initialCoins, int expectedCoins) {
        // Given
        var username = "ahmet";
        var userId = UUID.randomUUID();
        var user = User.builder()
                .username(username)
                .country(Country.TR)
                .id(userId)
                .coin(initialCoins)
                .level(35)
                .build();

        var updatedUser = User.builder()
                .username(username)
                .country(Country.TR)
                .id(userId)
                .coin(expectedCoins)
                .level(35)
                .build();

        var participant = Mockito.mock(Participant.class);

        var request = new ClaimRewardRequest(userId);

        var expected = ClaimRewardResponse.builder()
                .id(userId.toString())
                .username(username)
                .country(user.getCountry().name())
                .level(user.getLevel())
                .coin(expectedCoins)
                .build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(participantRepository.findParticipantWithReward(user)).thenReturn(Optional.of(participant));
        when(tournamentGroupService.getRankingOfUser(any(), eq(user))).thenReturn(rank);
        when(userMapper.convertToClaimRewardResponse(any(User.class))).thenReturn(expected);
        lenient().when(userRepository.save(user)).thenReturn(updatedUser);

        // When
        var actual = tournamentService.claimReward(request);

        //Then
        assertEquals(actual, expected);

        verify(userService, times(1)).getUserById(userId);
        verify(participantRepository, times(1)).findParticipantWithReward(user);
        verify(tournamentGroupService, times(1)).getRankingOfUser(any(), eq(user));
        verify(tournamentGroupService, times(1)).getRankingOfUser(any(), eq(user));
        verify(participant, times(1)).setHasReward(true);
        verify(participantRepository, times(1)).save(any());
        verify(userMapper, times(1)).convertToClaimRewardResponse(any(User.class));
    }

    @Test
    void claimReward_shouldThrowExceptionWhenNoRewardAvailable() {
        // Given
        var user = User.builder().build();
        var request = Mockito.mock(ClaimRewardRequest.class);

        when(userService.getUserById(any())).thenReturn(user);
        when(participantRepository.findParticipantWithReward(user)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NoRewardAvailableException.class, () -> tournamentService.claimReward(request));

        verify(userService, times(1)).getUserById(any());
        verify(participantRepository, times(1)).findParticipantWithReward(user);
    }


}
