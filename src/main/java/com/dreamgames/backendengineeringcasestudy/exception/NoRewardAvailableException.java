package com.dreamgames.backendengineeringcasestudy.exception;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseException;
import com.dreamgames.backendengineeringcasestudy.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class NoRewardAvailableException extends BaseException {
    public NoRewardAvailableException() {super(HttpStatus.NOT_FOUND, ErrorMessage.NO_REWARD_AVAILABLE);}
}
