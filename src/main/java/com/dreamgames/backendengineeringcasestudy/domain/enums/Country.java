package com.dreamgames.backendengineeringcasestudy.domain.enums;

public enum Country {
    UK("United Kingdom"),
    TR("Türkiye"),
    US("United States"),
    FR("France"),
    DE("Germany");

    public final String name;

    Country(String name) {
        this.name = name;
    }
}
