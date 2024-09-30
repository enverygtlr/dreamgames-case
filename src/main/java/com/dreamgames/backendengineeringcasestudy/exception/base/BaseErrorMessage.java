package com.dreamgames.backendengineeringcasestudy.exception.base;
import java.io.Serializable;

public interface BaseErrorMessage extends Serializable {

    String getTitle();
    String getMessage();
}
