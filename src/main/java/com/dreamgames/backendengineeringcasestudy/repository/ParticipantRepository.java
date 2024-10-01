package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    public Optional<Participant> findByUserAndTournament(User user, Tournament tournament);

    public boolean existsByUserAndRewardClaimedFalse(User user);

    public List<Participant> findByGroup(TournamentGroup group);

}

