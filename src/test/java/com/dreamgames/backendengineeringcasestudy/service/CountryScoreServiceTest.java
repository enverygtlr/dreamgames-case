package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.dto.CountryScoreRank;
import com.dreamgames.backendengineeringcasestudy.domain.entity.CountryScore;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.repository.CountryScoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryScoreServiceTest {
    @Mock
    private CountryScoreRepository countryScoreRepository;

    @InjectMocks
    private CountryScoreService countryScoreService;

    @Test
    void addCountryScore_shouldAddScoreToCountryScoreRepository() {
        // Given
        Tournament tournament = Tournament.builder().build();
        long countryScoreId = 1L;
        Country country = Country.US;
        int currentScore = 30;
        int score = 1;

        var initialCountryScore = CountryScore.builder()
                .id(countryScoreId)
                .country(country)
                .totalScore(currentScore)
                .tournament(tournament)
                .build();

        when(countryScoreRepository.findCountryScoreByCountryAndTournament(country, tournament)).thenReturn(Optional.of(initialCountryScore));

        ArgumentCaptor<CountryScore> countryScoreCaptor = ArgumentCaptor.forClass(CountryScore.class);

        // When
        countryScoreService.addCountryScore(tournament, country, score);

        // Then
        verify(countryScoreRepository, times(1)).findCountryScoreByCountryAndTournament(country, tournament);
        verify(countryScoreRepository, times(1)).save(countryScoreCaptor.capture());

        CountryScore capturedCountryScore = countryScoreCaptor.getValue();
        assertEquals(countryScoreId, capturedCountryScore.getId());
        assertEquals(country, capturedCountryScore.getCountry());
        assertEquals(currentScore + score, capturedCountryScore.getTotalScore());
        assertEquals(tournament, capturedCountryScore.getTournament());
    }

    @ParameterizedTest
    @MethodSource("com.dreamgames.backendengineeringcasestudy.service.CountryScoreServiceTestDataProvider#getCountryScoreLeaderboardTestDataStream")
    void getCountryScoreLeaderboard_shouldReturnLeaderboard(CountryScoreServiceTestDataProvider.GetCountryScoreLeaderboardTestData getCountryScoreLeaderboardTestData) {
        // Given
        Tournament tournament = getCountryScoreLeaderboardTestData.tournament();
        var countryScores = getCountryScoreLeaderboardTestData.countryScores();
        var expectedRanks = getCountryScoreLeaderboardTestData.expectedRanks();

        when(countryScoreRepository.findCountryScoreByTournament(tournament)).thenReturn(countryScores);

        // When
        var actualRanks = countryScoreService.getCountryScoreLeaderboard(tournament);

        assertTrue(haveSameElements(actualRanks, expectedRanks));
    }

    private static boolean haveSameElements(List<CountryScoreRank> list1, List<CountryScoreRank> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        return list1.containsAll(list2) && list2.containsAll(list1);
    }
}
