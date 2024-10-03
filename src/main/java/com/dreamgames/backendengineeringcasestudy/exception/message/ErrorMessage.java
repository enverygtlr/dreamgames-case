package com.dreamgames.backendengineeringcasestudy.exception.message;

import com.dreamgames.backendengineeringcasestudy.exception.base.BaseErrorMessage;
import lombok.Getter;

@Getter
public enum ErrorMessage implements BaseErrorMessage {
    INVALID_LEVEL("New level value is less or equal to current level.", "Invalid new level value."),
    NO_REWARD_AVAILABLE("No Reward Available", "Tournament Reward Exception."),
    DUPLICATE_USER("User with given username already exists.", "Existing Username"),
    INVALID_USER("User with given id does not exist.", "Invalid User"),
    MISSING_REQ("User does not have enough coins or levels to join the tournament.", "Missing Requirements"),
    TOURNAMENT_NOT_FOUND("There is not available a tournament at the moment.", "Tournament not found"),
    ALREADY_IN_TOURNAMENT("User already in current tournament.", "User in Tournament"),
    UNFINISHED_TOURNAMENT("User has unclaimed reward from previous tournament.", "Unclaimed reward.");



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
