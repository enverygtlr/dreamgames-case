package com.dreamgames.backendengineeringcasestudy.exception;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseException;
import com.dreamgames.backendengineeringcasestudy.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class InvalidLevelValueException extends BaseException {
    public InvalidLevelValueException() {super(HttpStatus.FORBIDDEN, ErrorMessage.INVALID_LEVEL);}
}