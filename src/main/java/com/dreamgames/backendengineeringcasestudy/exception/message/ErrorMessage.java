package com.dreamgames.backendengineeringcasestudy.exception.message;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseErrorMessage;
import lombok.Getter;

@Getter
public enum ErrorMessage implements BaseErrorMessage {
    DUPLICATE_USER("User with given username already exists.", "Existing Username"),
    INVALID_USER("User with given id does not exist.", "Invalid User");

    private final String message;
    private final String title;

    ErrorMessage(String message) {
        this.message = message;
        this.title = null;
    }

    ErrorMessage(String message, String title) {
        this.message = message;
        this.title = title;
    }

    @Override
    public String toString() {
        return title + " | " + message;
    }
}
