package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserUpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserUpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.exception.UserDuplicateException;
import com.dreamgames.backendengineeringcasestudy.exception.UserNotExistException;
import com.dreamgames.backendengineeringcasestudy.mapper.UserMapper;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void save_shouldSuccess() {
        // Given
        var userId = UUID.randomUUID();
        var username = "ahmet";
        var request = new UserSaveRequest(username);

        var user = User.builder()
                .id(userId)
                .country(Country.US)
                .username(username)
                .build();

        var expected = UserResponse.builder()
                .id(user.getId().toString())
                .username(username)
                .country(user.getCountry().name())
                .level(user.getLevel())
                .coin(user.getCoin())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.convertToUserResponse(user)).thenReturn(expected);

        //Where
        var actual = userService.save(request);

        //Then
        assertEquals(expected, actual);
    }

    @Test
    void save_shouldThrowWhenUserAlreadyExists() {
        UserSaveRequest request = new UserSaveRequest("duplicateUser");

        when(userRepository.existsByUsername("duplicateUser")).thenReturn(true);

        assertThrows(UserDuplicateException.class, () -> userService.save(request));
        verify(userRepository, times(1)).existsByUsername("duplicateUser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_shouldSuccess() {
        // Given
        int newLevel = 30;
        UserUpdateLevelRequest request = new UserUpdateLevelRequest(UUID.randomUUID(), newLevel);
        User user = User.builder()
                .id(request.id())
                .country(Country.TR)
                .username("testUser")
                .level(5)
                .coin(100)
                .build();

        User updatedUser = User.builder()
                .id(user.getId())
                .country(user.getCountry())
                .username(user.getUsername())
                .level(5 + newLevel)
                .coin(100 + (25 * newLevel))
                .build();

        UserUpdateLevelResponse response = new UserUpdateLevelResponse(updatedUser.getId().toString(),"testUser", updatedUser.getCountry().name(), updatedUser.getLevel(), updatedUser.getCoin());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.convertToUserUpdateLevelResponse(updatedUser)).thenReturn(response);

        // When
        UserUpdateLevelResponse result = userService.update(request);

        // Then
        assertNotNull(result);
        assertEquals(updatedUser.getLevel(), result.level());
        assertEquals(updatedUser.getCoin(), result.coin());

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).convertToUserUpdateLevelResponse(updatedUser);
    }

    @Test
    void update_shouldThrowWhenUserDoesNotExist() {
        // Given
       var userId = UUID.randomUUID();
       var request = new UserUpdateLevelRequest(userId, 0);

       when(userRepository.findById(userId)).thenReturn(Optional.empty());

       //When - Then
        assertThrows(UserNotExistException.class, () -> userService.update(request));
    }
}