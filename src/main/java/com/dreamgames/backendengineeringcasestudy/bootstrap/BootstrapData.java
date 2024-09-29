package com.dreamgames.backendengineeringcasestudy.bootstrap;

import com.dreamgames.backendengineeringcasestudy.domain.entity.*;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentGroupRepository tournamentGroupRepository;
    private final ParticipantRepository participantRepository;
    private final CountryScoreRepository countryScoreRepository;

    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        User user1 = User.builder()
                .username("player1")
                .coin(5000)
                .country(Country.TR)
                .level(20)
                .build();

        User user2 = User.builder()
                .username("player2")
                .coin(5000)
                .country(Country.FR)
                .level(20)
                .build();

        User user3 = User.builder()
                .username("player3")
                .coin(5000)
                .country(Country.DE)
                .level(20)
                .build();


        User user4 = User.builder()
                .username("player4")
                .coin(5000)
                .country(Country.UK)
                .level(20)
                .build();

        User user5 = User.builder()
                .username("player5")
                .coin(5000)
                .country(Country.US)
                .level(20)
                .build();

        User user6 = User.builder()
                .username("player6")
                .coin(5000)
                .country(Country.TR)
                .level(1)
                .build();

        User user7 = User.builder()
                .username("player7")
                .coin(5000)
                .country(Country.FR)
                .level(1)
                .build();

        Tournament tournament1 = Tournament.builder()
                .startTime(LocalDateTime.of(2024, Month.SEPTEMBER, 29, 0, 0))
                .end_time(LocalDateTime.of(2024, Month.SEPTEMBER, 29, 20, 0))
                .isActive(true)
                .build();

        Tournament tournament2 = Tournament.builder()
                .startTime(LocalDateTime.of(2024, Month.SEPTEMBER, 28, 0, 0))
                .end_time(LocalDateTime.of(2024, Month.SEPTEMBER, 28, 20, 0))
                .isActive(false)
                .build();

        Tournament tournament3 = Tournament.builder()
                .startTime(LocalDateTime.of(2024, Month.SEPTEMBER, 27, 0, 0))
                .end_time(LocalDateTime.of(2024, Month.SEPTEMBER, 27, 20, 0))
                .isActive(false)
                .build();

        TournamentGroup group1 = TournamentGroup.builder()
                .tournament(tournament1)
                .ready(false)
                .build();

        TournamentGroup group2 = TournamentGroup.builder()
                .tournament(tournament1)
                .ready(false)
                .build();

        TournamentGroup group3 = TournamentGroup.builder()
                .tournament(tournament1)
                .ready(false)
                .build();

        Participant participant1 = Participant.builder()
                .user(user1)
                .country(user1.getCountry())
                .group(group1)
                .tournament(tournament3)
                .score(0)
                .rewardClaimed(false)
                .build();

        Participant participant2 = Participant.builder()
                .user(user2)
                .country(user2.getCountry())
                .group(group1)
                .tournament(tournament3)
                .score(0)
                .rewardClaimed(false)
                .build();

        Participant participant3 = Participant.builder()
                .user(user3)
                .country(user3.getCountry())
                .group(group1)
                .tournament(tournament3)
                .score(0)
                .rewardClaimed(false)
                .build();

        Participant participant4 = Participant.builder()
                .user(user4)
                .country(user4.getCountry())
                .group(group1)
                .tournament(tournament3)
                .score(0)
                .rewardClaimed(false)
                .build();

        Participant participant5 = Participant.builder()
                .user(user5)
                .country(user5.getCountry())
                .group(group1)
                .tournament(tournament3)
                .score(0)
                .rewardClaimed(false)
                .build();

        Participant participant6 = Participant.builder()
                .user(user6)
                .group(group2)
                .tournament(tournament3)
                .score(0)
                .rewardClaimed(false)
                .country(user6.getCountry())
                .build();

        CountryScore score1 = CountryScore.builder()
                .country(Country.TR)
                .tournament(tournament3)
                .totalScore(350)
                .build();

        CountryScore score2 = CountryScore.builder()
                .country(Country.FR)
                .tournament(tournament3)
                .totalScore(250)
                .build();

        CountryScore score3 = CountryScore.builder()
                .country(Country.DE)
                .tournament(tournament3)
                .totalScore(310)
                .build();

        CountryScore score4 = CountryScore.builder()
                .country(Country.UK)
                .tournament(tournament3)
                .totalScore(370)
                .build();

        CountryScore score5 = CountryScore.builder()
                .country(Country.US)
                .tournament(tournament3)
                .totalScore(120)
                .build();

        if (userRepository.count() == 0) {
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
            userRepository.save(user7);
        }

        if (tournamentRepository.count() == 0) {
            tournamentRepository.save(tournament1);
            tournamentRepository.save(tournament2);
            tournamentRepository.save(tournament3);
        }

        if (tournamentGroupRepository.count() == 0) {
            tournamentGroupRepository.save(group1);
            tournamentGroupRepository.save(group2);
            tournamentGroupRepository.save(group3);
        }

        if (participantRepository.count() == 0) {
            participantRepository.save(participant1);
            participantRepository.save(participant2);
            participantRepository.save(participant3);
            participantRepository.save(participant4);
            participantRepository.save(participant5);
            participantRepository.save(participant6);
        }

        if (countryScoreRepository.count() == 0) {
            countryScoreRepository.save(score1);
            countryScoreRepository.save(score2);
            countryScoreRepository.save(score3);
            countryScoreRepository.save(score4);
            countryScoreRepository.save(score5);
        }
    }

}
