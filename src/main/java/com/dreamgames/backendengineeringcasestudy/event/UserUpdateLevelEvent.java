package com.dreamgames.backendengineeringcasestudy.event;

import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import org.springframework.context.ApplicationEvent;

public class UserUpdateLevelEvent extends ApplicationEvent {
    private User user;

    public UserUpdateLevelEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
