package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.dto.CountryScoreRank;
import com.dreamgames.backendengineeringcasestudy.domain.dto.ParticipantRank;
import com.dreamgames.backendengineeringcasestudy.domain.entity.CountryScore;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.exception.TournamentNotFoundException;
import com.dreamgames.backendengineeringcasestudy.repository.CountryScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryScoreService {
    private final CountryScoreRepository countryScoreRepository;

    public void addCountryScore(Tournament tournament, Country country, int score) {
        var countryScore = countryScoreRepository.findCountryScoreByCountryAndTournament(country, tournament).orElseThrow(RuntimeException::new);
        countryScore.setTotalScore(countryScore.getTotalScore() + score);
        countryScoreRepository.save(countryScore);
    }

    public List<CountryScoreRank> getCountryScoreLeaderboard(Tournament tournament) {
        var countryScores = countryScoreRepository.findCountryScoreByTournament(tournament);
        return getCountryScoreRanks(countryScores);
    }

    private List<CountryScoreRank> getCountryScoreRanks(List<CountryScore> countryScores) {
        var sortedCountryScores = countryScores.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getTotalScore(), c1.getTotalScore()))
                .toList();

        List<CountryScoreRank> countryScoreRanks = new ArrayList<>();
        int rank = 1;
        for (int i = 0; i < sortedCountryScores.size(); i++) {
            if (i > 0 && !sortedCountryScores.get(i).getTotalScore().equals(sortedCountryScores.get(i - 1).getTotalScore())) {
                rank = rank + 1;
            }
            countryScoreRanks.add(new CountryScoreRank(rank, sortedCountryScores.get(i).getTotalScore(), sortedCountryScores.get(i).getCountry()));
        }
        return countryScoreRanks;
    }
}
