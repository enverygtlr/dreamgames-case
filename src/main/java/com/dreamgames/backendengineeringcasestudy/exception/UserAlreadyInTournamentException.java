package com.dreamgames.backendengineeringcasestudy.exception;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseException;
import com.dreamgames.backendengineeringcasestudy.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class UserAlreadyInTournamentException extends BaseException {
    public UserAlreadyInTournamentException() {super(HttpStatus.CONFLICT, ErrorMessage.ALREADY_IN_TOURNAMENT);}
}
