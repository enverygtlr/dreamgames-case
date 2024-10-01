package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.domain.request.ClaimRewardRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.EnterTournamentRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.GroupLeaderboardRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.ClaimRewardResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.EnterTournamentResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.GroupLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {
    private final TournamentService tournamentService;

    @PostMapping("/enter")
    public ResponseEntity<EnterTournamentResponse> enterTournament(@RequestBody EnterTournamentRequest request) {
        return ResponseEntity.ok(tournamentService.enterTournament(request));
    }

    @PostMapping("/claim-reward")
    public ResponseEntity<ClaimRewardResponse> claimReward(@RequestBody ClaimRewardRequest request) {
       return ResponseEntity.ok(tournamentService.claimReward(request));
    }

    @GetMapping("/group-leaderboard")
    public ResponseEntity<GroupLeaderboardResponse> getGroupLeaderboard(@RequestBody GroupLeaderboardRequest request) {
        return ResponseEntity.ok(tournamentService.getGroupLeaderboard(request));
    }


}
