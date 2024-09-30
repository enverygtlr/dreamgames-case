package com.dreamgames.backendengineeringcasestudy.exception;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseException;
import com.dreamgames.backendengineeringcasestudy.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class UserDuplicateException extends BaseException {
    public UserDuplicateException() {super(HttpStatus.CONFLICT, ErrorMessage.DUPLICATE_USER);}
}