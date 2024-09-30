package com.dreamgames.backendengineeringcasestudy.domain.enums;

import java.util.Random;

public enum Country {
    UK("United Kingdom"),
    TR("Türkiye"),
    US("United States"),
    FR("France"),
    DE("Germany");

    public final String fullName;

    Country(String fullName) {
        this.fullName = fullName;
    }

    private static final Random PRNG = new Random();

    public static Country getRandomCountry() {
        Country[] countries = values();
        return countries[PRNG.nextInt(countries.length)];
    }
}
