package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.domain.request.EnterTournamentRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.EnterTournamentResponse;
import com.dreamgames.backendengineeringcasestudy.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {
    private final TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<EnterTournamentResponse> enterTournament(@RequestBody EnterTournamentRequest request) {
        return ResponseEntity.ok(tournamentService.enterTournament(request));
    }
}
