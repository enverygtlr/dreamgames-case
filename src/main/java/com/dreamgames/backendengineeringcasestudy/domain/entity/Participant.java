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
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tournament tournament;

    @NotNull
    private Integer score;

    @NotNull
    @JdbcTypeCode(value = SqlTypes.VARCHAR)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private TournamentGroup group;

    @NotNull
    @Column(name = "reward_claimed", nullable = false)
    private Boolean rewardClaimed;
}
