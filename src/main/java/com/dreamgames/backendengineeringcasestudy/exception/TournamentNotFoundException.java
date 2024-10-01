package com.dreamgames.backendengineeringcasestudy.exception;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseException;
import com.dreamgames.backendengineeringcasestudy.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class TournamentNotFoundException extends BaseException {
    public TournamentNotFoundException() {super(HttpStatus.NOT_FOUND, ErrorMessage.TOURNAMENT_NOT_FOUND);}
}
