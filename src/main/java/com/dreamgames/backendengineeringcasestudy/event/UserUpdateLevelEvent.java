package com.dreamgames.backendengineeringcasestudy.event;

import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserUpdateLevelEvent extends ApplicationEvent {
    private final User user;

    public UserUpdateLevelEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

}
