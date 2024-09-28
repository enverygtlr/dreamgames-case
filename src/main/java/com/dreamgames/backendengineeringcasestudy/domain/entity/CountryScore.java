package com.dreamgames.backendengineeringcasestudy.domain.entity;

import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CountryScore")
public class CountryScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JdbcTypeCode(value = SqlTypes.VARCHAR)
    private Country country;

    @NotNull
    @Column(name = "total_score")
    private Integer totalScore;

    @ManyToOne
    private Tournament tournament;
}
