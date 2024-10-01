package com.dreamgames.backendengineeringcasestudy.exception;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseException;
import com.dreamgames.backendengineeringcasestudy.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class MissingRequirementException extends BaseException {
    public MissingRequirementException() {super(HttpStatus.FORBIDDEN, ErrorMessage.MISSING_REQ);}
}