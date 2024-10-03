package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.domain.entity.Tournament;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Integer> {

    @Query(value = """
            select tg from TournamentGroup tg 
            left join Participant p on tg.id = p.group.id and p.country = :country
            where p.id is NULL and tg.ready = false and tg.tournament = :tournament
            """)
    List<TournamentGroup> findAvailableGroups(Country country, Tournament tournament);

    default Optional<TournamentGroup> findAvailableGroup(Country country, Tournament tournament) {
        return findAvailableGroups(country, tournament).stream().findFirst();
    }

    List<TournamentGroup> findAllByTournament(Tournament tournament);

}
