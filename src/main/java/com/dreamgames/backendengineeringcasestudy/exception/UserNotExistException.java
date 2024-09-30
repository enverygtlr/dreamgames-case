package com.dreamgames.backendengineeringcasestudy.exception;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseException;
import com.dreamgames.backendengineeringcasestudy.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class UserNotExistException extends BaseException {
    public UserNotExistException() {super(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_USER);}
}
