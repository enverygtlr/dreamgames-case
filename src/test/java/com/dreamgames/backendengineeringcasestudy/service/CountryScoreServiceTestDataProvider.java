package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.dto.CountryScoreRank;
import com.dreamgames.backendengineeringcasestudy.domain.entity.CountryScore;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import lombok.Builder;

import java.util.List;
import java.util.stream.Stream;

public class CountryScoreServiceTestDataProvider {
    @Builder
    public record GetCountryScoreLeaderboardTestData(
           Tournament tournament,
           List<CountryScore> countryScores,
           List<CountryScoreRank> expectedRanks
    ) {}

    static Stream<GetCountryScoreLeaderboardTestData> getCountryScoreLeaderboardTestDataStream() {
        return Stream.of(getCountryScoreLeaderboard_case1(), getCountryScoreLeaderboard_case2());
    }

    private static GetCountryScoreLeaderboardTestData getCountryScoreLeaderboard_case1() {
        Tournament tournament = Tournament.builder().build();

        var countryScore1 = CountryScore.builder()
                .id(1L)
                .country(Country.TR)
                .totalScore(200)
                .tournament(tournament)
                .build();

        var countryScore2 =  CountryScore.builder()
                .id(2L)
                .country(Country.US)
                .totalScore(150)
                .tournament(tournament)
                .build();

        var countryScore3 =  CountryScore.builder()
                .id(3L)
                .country(Country.UK)
                .totalScore(125)
                .tournament(tournament)
                .build();

        var countryScore4 =  CountryScore.builder()
                .id(4L)
                .country(Country.DE)
                .totalScore(100)
                .tournament(tournament)
                .build();


        List<CountryScore> countryScores = List.of(countryScore1, countryScore2, countryScore3, countryScore4);

        List<CountryScoreRank> expectedRanks = List.of(
                CountryScoreRank.builder().rank(1)
                        .score(countryScore1.getTotalScore())
                        .country(countryScore1.getCountry())
                        .build(),
                CountryScoreRank.builder().rank(2)
                        .score(countryScore2.getTotalScore())
                        .country(countryScore2.getCountry())
                        .build(),
                CountryScoreRank.builder().rank(3)
                        .score(countryScore3.getTotalScore())
                        .country(countryScore3.getCountry())
                        .build(),
                CountryScoreRank.builder().rank(4)
                        .score(countryScore4.getTotalScore())
                        .country(countryScore4.getCountry())
                        .build()
        );

        return new GetCountryScoreLeaderboardTestData(tournament, countryScores, expectedRanks);
    }

    private static GetCountryScoreLeaderboardTestData getCountryScoreLeaderboard_case2() {
        Tournament tournament = Tournament.builder().build();

        var countryScore1 = CountryScore.builder()
                .id(1L)
                .country(Country.TR)
                .totalScore(200)
                .tournament(tournament)
                .build();

        var countryScore2 =  CountryScore.builder()
                .id(2L)
                .country(Country.US)
                .totalScore(200)
                .tournament(tournament)
                .build();

        var countryScore3 =  CountryScore.builder()
                .id(3L)
                .country(Country.UK)
                .totalScore(125)
                .tournament(tournament)
                .build();

        var countryScore4 =  CountryScore.builder()
                .id(4L)
                .country(Country.DE)
                .totalScore(125)
                .tournament(tournament)
                .build();


        List<CountryScore> countryScores = List.of(countryScore1, countryScore2, countryScore3, countryScore4);

        List<CountryScoreRank> expectedRanks = List.of(
                CountryScoreRank.builder().rank(1)
                        .score(countryScore1.getTotalScore())
                        .country(countryScore1.getCountry())
                        .build(),
                CountryScoreRank.builder().rank(1)
                        .score(countryScore2.getTotalScore())
                        .country(countryScore2.getCountry())
                        .build(),
                CountryScoreRank.builder().rank(2)
                        .score(countryScore3.getTotalScore())
                        .country(countryScore3.getCountry())
                        .build(),
                CountryScoreRank.builder().rank(2)
                        .score(countryScore4.getTotalScore())
                        .country(countryScore4.getCountry())
                        .build()
        );

        return new GetCountryScoreLeaderboardTestData(tournament, countryScores, expectedRanks);
    }
}
