package com.dreamgames.backendengineeringcasestudy.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tournament_group")
public class TournamentGroup {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    private Boolean ready;

    @ManyToOne
    private Tournament tournament;

    @Override
    public String toString() {
        return "TournamentGroup{" +
                "id=" + id +
                ", ready=" + ready +
                ", tournament=" + tournament +
                '}';
    }
}
