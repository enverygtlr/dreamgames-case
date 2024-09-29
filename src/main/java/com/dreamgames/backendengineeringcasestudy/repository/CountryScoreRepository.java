package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.domain.entity.CountryScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryScoreRepository extends JpaRepository<CountryScore, Long> {
}
