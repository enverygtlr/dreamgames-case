package com.dreamgames.backendengineeringcasestudy.domain.dto;

import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;

public record CountryScoreRank(
        int rank,
        int score,
        Country country
){
}
