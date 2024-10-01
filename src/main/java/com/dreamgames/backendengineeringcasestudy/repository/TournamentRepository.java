package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
    Optional<Tournament> findFirstByIsActiveTrue();
}
