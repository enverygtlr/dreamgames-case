package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.mapper.ParticipantMapper;
import com.dreamgames.backendengineeringcasestudy.repository.ParticipantRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TournamentGroupServiceTest {
    @Mock
    private TournamentGroupRepository tournamentGroupRepository;

    @Mock
    private  ParticipantRepository participantRepository;

    @Mock
    private  ParticipantMapper participantMapper;

    @InjectMocks
    private TournamentGroupService tournamentGroupService;

    @Test
    void assignUserToTournamentGroup_shouldSuccessWhenThereIsAvailableGroup() {
        //Given
        Tournament tournament = Mockito.mock(Tournament.class);
        TournamentGroup group = Mockito.mock(TournamentGroup.class);
        var userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .country(Country.TR)
                .username("testUser")
                .level(5)
                .coin(100)
                .build();

        List<Participant> mockParticipants = Mockito.mock(List.class); // Mock the list of participants
        when(mockParticipants.size()).thenReturn(5);

        when(tournamentGroupRepository.findAvailableGroup(user.getCountry(), tournament)).thenReturn(Optional.of(group));
        when(participantRepository.findByGroup(group)).thenReturn(mockParticipants);

        //When
        var result = tournamentGroupService.assignUserToTournamentGroup(tournament, user);

        // Then
        assertEquals(result, group);

        verify(tournamentGroupRepository, times(1)).findAvailableGroup(user.getCountry(), tournament);
        verify(participantRepository, times(1)).findByGroup(group);
        verify(participantRepository, times(1)).save(any(Participant.class));
        verify(group, times(1)).setReady(true);
        verify(tournamentGroupRepository, times(1)).save(group);
    }

    @Test
    void assignUserToTournamentGroup_shouldGenerateGroupWhenThereIsNotAvailableGroup() {
        Tournament tournament = Mockito.mock(Tournament.class);
        TournamentGroup group = Mockito.mock(TournamentGroup.class);
        var userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .country(Country.TR)
                .username("testUser")
                .level(5)
                .coin(100)
                .build();

        when(tournamentGroupRepository.findAvailableGroup(user.getCountry(), tournament)).thenReturn(Optional.empty());

        //When
        tournamentGroupService.assignUserToTournamentGroup(tournament, user);

        //Then
        verify(tournamentGroupRepository, times(1)).save(any(TournamentGroup.class));
    }

    @ParameterizedTest
    @MethodSource("com.dreamgames.backendengineeringcasestudy.service.TournamentGroupServiceTestDataProvider#generateGetRankingsTestData")
    void getRankings_shouldCalculateCorrectRankings(TournamentGroupServiceTestDataProvider.GetRankingsTestData rankingTestData) {
        // Given
        var tournamentGroup = rankingTestData.tournamentGroup();
        var participants = rankingTestData.participants();
        var expectedParticipantRanks = rankingTestData.expectedParticipantRanks();

        when(participantRepository.findByGroup(tournamentGroup)).thenReturn(participants);

        // When
        tournamentGroupService.getRankings(tournamentGroup);

        // Then
        expectedParticipantRanks.stream().forEach(expectedParticipantRank -> {
            verify(participantMapper).toGroupRankDTO(expectedParticipantRank.participant(), expectedParticipantRank.rank());
        });
    }

    @ParameterizedTest
    @MethodSource("com.dreamgames.backendengineeringcasestudy.service.TournamentGroupServiceTestDataProvider#generateGetRankingOfUserTestData")
    void getRankingOfUser_shouldReturnCorrectRankingOfUser(TournamentGroupServiceTestDataProvider.GetRankingOfUserTestData rankingOfUserTestData) {
        // Given
        var user = rankingOfUserTestData.user();
        var tournamentGroup = rankingOfUserTestData.tournamentGroup();
        var participants = rankingOfUserTestData.participants();
        var expectedRank = rankingOfUserTestData.expectedRank();

        when(participantRepository.findByGroup(tournamentGroup)).thenReturn(participants);

        //When
        int actualRank = tournamentGroupService.getRankingOfUser(tournamentGroup, user);

        //Then
        assertEquals(expectedRank, actualRank);
    }
}
