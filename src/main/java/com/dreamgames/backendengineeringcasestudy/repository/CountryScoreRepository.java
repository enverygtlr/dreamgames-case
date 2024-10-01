package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.domain.entity.CountryScore;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryScoreRepository extends JpaRepository<CountryScore, Long> {
    List<CountryScore> findCountryScoreByTournament(Tournament tournament);

    Optional<CountryScore> findCountryScoreByCountryAndTournament(Country country, Tournament tournament);
}
