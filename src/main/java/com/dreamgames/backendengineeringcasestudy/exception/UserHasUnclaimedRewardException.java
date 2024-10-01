package com.dreamgames.backendengineeringcasestudy.exception;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseException;
import com.dreamgames.backendengineeringcasestudy.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class UserHasUnclaimedRewardException extends BaseException {
    public UserHasUnclaimedRewardException () {super(HttpStatus.CONFLICT, ErrorMessage.UNFINISHED_TOURNAMENT);}
}
