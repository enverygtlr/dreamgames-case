package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.dto.ParticipantRank;
import com.dreamgames.backendengineeringcasestudy.domain.entity.Participant;
import com.dreamgames.backendengineeringcasestudy.domain.entity.TournamentGroup;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import lombok.Builder;

import java.util.List;
import java.util.stream.Stream;

public class TournamentGroupServiceTestDataProvider {
    @Builder
    public record GetRankingOfUserTestData(
            TournamentGroup tournamentGroup,
            List<Participant> participants,
            User user,
            int expectedRank
    ){}

    @Builder
    public record GetRankingsTestData(
            TournamentGroup tournamentGroup,
            List<Participant> participants,
            List<ParticipantRank> expectedParticipantRanks
    ) {}

    static Stream<GetRankingsTestData> generateGetRankingsTestData() {
        return Stream.of(getRankings_case1(), getRankings_case2());
    }

    static Stream<GetRankingOfUserTestData> generateGetRankingOfUserTestData() {
        return Stream.of(getRankingOfUser_case1(), getRankingOfUser_case2());
    }

    private static GetRankingOfUserTestData getRankingOfUser_case1() {
        User user = User.builder().build();
        TournamentGroup group = TournamentGroup.builder().build();

        var participant1 = Participant.builder().user(user).group(group).score(8).build();
        var participant2 = Participant.builder().user(new User()).group(group).score(8).build();
        var participant3 = Participant.builder().user(new User()).group(group).score(9).build();

        List<Participant> participants = List.of(participant1,participant2, participant3);

        int expectedRank = 2;

        return new GetRankingOfUserTestData(group, participants, user, expectedRank);
    }

    private static GetRankingOfUserTestData getRankingOfUser_case2() {
        User user = User.builder().build();
        TournamentGroup group = TournamentGroup.builder().build();

        var participant1 = Participant.builder().user(user).group(group).score(1).build();
        var participant2 = Participant.builder().user(new User()).group(group).score(2).build();
        var participant3 = Participant.builder().user(new User()).group(group).score(3).build();
        var participant4 = Participant.builder().user(new User()).group(group).score(4).build();
        var participant5 = Participant.builder().user(new User()).group(group).score(4).build();

        List<Participant> participants = List.of(participant1,participant2, participant3, participant4, participant5);

        int expectedRank = 4;

        return new GetRankingOfUserTestData(group, participants, user, expectedRank);
    }


    private static GetRankingsTestData getRankings_case1() {
        TournamentGroup group = TournamentGroup.builder().build();
        var participant1 = Participant.builder().group(group).score(10).build();
        var participant2 = Participant.builder().group(group).score(10).build();
        var participant3 = Participant.builder().group(group).score(8).build();
        var participant4 = Participant.builder().group(group).score(8).build();

        List<Participant> participants = List.of(participant1,participant2, participant3, participant4);

        List<ParticipantRank> expectedParticipantRanks = List.of(
                ParticipantRank.builder() .participant(participant1).rank(1).build(),
                ParticipantRank.builder() .participant(participant2).rank(1).build(),
                ParticipantRank.builder() .participant(participant3).rank(2).build(),
                ParticipantRank.builder() .participant(participant4).rank(2).build()
        );

        return new GetRankingsTestData(group, participants, expectedParticipantRanks);
    }

    private static GetRankingsTestData getRankings_case2() {
        TournamentGroup group = TournamentGroup.builder().build();
        var participant1 = Participant.builder().group(group).score(3).build();
        var participant2 = Participant.builder().group(group).score(5).build();
        var participant3 = Participant.builder().group(group).score(5).build();
        var participant4 = Participant.builder().group(group).score(4).build();
        var participant5 = Participant.builder().group(group).score(19).build();

        List<Participant> participants = List.of(participant1,participant2, participant3, participant4, participant5);

        List<ParticipantRank> expectedParticipantRanks = List.of(
                ParticipantRank.builder().participant(participant1).rank(4).build(),
                ParticipantRank.builder().participant(participant2).rank(2).build(),
                ParticipantRank.builder().participant(participant3).rank(2).build(),
                ParticipantRank.builder().participant(participant4).rank(3).build(),
                ParticipantRank.builder().participant(participant5).rank(1).build()
        );

        return new GetRankingsTestData(group, participants, expectedParticipantRanks);
    }




}
