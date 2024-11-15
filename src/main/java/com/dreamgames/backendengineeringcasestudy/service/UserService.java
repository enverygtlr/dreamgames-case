package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.constants.ApplicationConstants;
import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserUpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserUpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.event.UserUpdateLevelEvent;
import com.dreamgames.backendengineeringcasestudy.exception.InvalidLevelValueException;
import com.dreamgames.backendengineeringcasestudy.exception.UserDuplicateException;
import com.dreamgames.backendengineeringcasestudy.exception.UserNotExistException;
import com.dreamgames.backendengineeringcasestudy.mapper.UserMapper;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public UserResponse save(UserSaveRequest request) {
        validateUserUniqueness(request);
        User newUser = userRepository.save(generateInitialUser(request));
        return userMapper.convertToUserResponse(newUser);
    }

    @Transactional
    public UserUpdateLevelResponse update(UserUpdateLevelRequest request) {
        User user = userRepository.findById(request.id()).orElseThrow(UserNotExistException::new);
        int levelDiff = getLevelDifference(request, user);
        user.setLevel(user.getLevel() + levelDiff);
        user.setCoin(user.getCoin() + (ApplicationConstants.LEVEL_PRIZE * levelDiff));
        User updatedUser = userRepository.save(user);
        applicationEventPublisher.publishEvent(new UserUpdateLevelEvent(this, updatedUser, levelDiff));
        return userMapper.convertToUserUpdateLevelResponse(updatedUser);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotExistException::new);
    }

    private User generateInitialUser(UserSaveRequest request) {
        return User.builder()
                .username(request.username())
                .country(Country.getRandomCountry())
                .level(ApplicationConstants.INITIAL_LEVEL)
                .coin(ApplicationConstants.INITIAL_COIN)
                .build();
    }

    private void validateUserUniqueness(UserSaveRequest request) {
       if(userRepository.existsByUsername(request.username())) {
           throw new UserDuplicateException();
       }
    }

    private int getLevelDifference(UserUpdateLevelRequest request, User user) {
        if (request.newLevel() != 0 && user.getLevel() >= request.newLevel()) throw new InvalidLevelValueException();
        else if (request.newLevel() == 0) return 1;
        else return request.newLevel() - user.getLevel();
    }
}
