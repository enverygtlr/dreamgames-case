package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    Optional<Participant> findByUserAndTournament(User user, Tournament tournament);

    default boolean existsUserWithUnclaimedReward(User user) {
        return existsByUserAndHasRewardTrue(user);
    };

    boolean existsByUserAndHasRewardTrue(User user);

    List<Participant> findByGroup(TournamentGroup group);

    Optional<Participant> findFirstByUserAndHasRewardTrue(User user);

    default Optional<Participant> findParticipantWithReward(User user) {
        return findFirstByUserAndHasRewardTrue(user);
    };

}

