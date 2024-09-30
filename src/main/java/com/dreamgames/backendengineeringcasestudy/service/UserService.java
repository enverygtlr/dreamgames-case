package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserResponse;
import com.dreamgames.backendengineeringcasestudy.exception.UserDuplicateException;
import com.dreamgames.backendengineeringcasestudy.mapper.UserMapper;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final Integer INITIAL_LEVEL = 1;
    private final Integer INITIAL_COIN = 5000;

    @Transactional
    public UserResponse save(UserSaveRequest request) {
        validateUserUniqueness(request);
        User newUser = userRepository.save(generateInitialUser(request));
        return userMapper.convertToUserResponse(newUser);
    }

    private User generateInitialUser(UserSaveRequest request) {
        return User.builder()
                .username(request.username())
                .country(Country.getRandomCountry())
                .level(INITIAL_LEVEL)
                .coin(INITIAL_COIN)
                .build();
    }

    private void validateUserUniqueness(UserSaveRequest request) {
       if(userRepository.existsByUsername(request.username())) {
           throw new UserDuplicateException();
       }
    }
}