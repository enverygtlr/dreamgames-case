package com.dreamgames.backendengineeringcasestudy.domain.entity;

import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 16, columnDefinition = "varchar(16)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String username;

    @NotNull
    @JdbcTypeCode(value = SqlTypes.VARCHAR)
    private Country country;

    @NotNull
    private Integer level;

    @NotNull
    private Integer coin;

}