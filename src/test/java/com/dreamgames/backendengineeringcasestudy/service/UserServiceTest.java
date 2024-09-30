package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserUpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserUpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.exception.UserDuplicateException;
import com.dreamgames.backendengineeringcasestudy.mapper.UserMapper;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSaveUserAlreadyExists() {
        UserSaveRequest request = new UserSaveRequest("duplicateUser");

        when(userRepository.existsByUsername("duplicateUser")).thenReturn(true);

        assertThrows(UserDuplicateException.class, () -> userService.save(request));
        verify(userRepository, times(1)).existsByUsername("duplicateUser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateSuccess() {
        // Given
        UserUpdateLevelRequest request = new UserUpdateLevelRequest(UUID.randomUUID()); // Assuming request contains user ID
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
                .level(6)
                .coin(125)
                .build();

        UserUpdateLevelResponse response = new UserUpdateLevelResponse(updatedUser.getId().toString(),"testUser", updatedUser.getCountry().name(), updatedUser.getLevel(), updatedUser.getCoin());


        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.convertToUserUpdateLevelResponse(updatedUser)).thenReturn(response);

        // When
        UserUpdateLevelResponse result = userService.update(request);

        // Then
        assertNotNull(result);
        assertEquals(6, result.level());
        assertEquals(125, result.coin());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).convertToUserUpdateLevelResponse(updatedUser);
    }
}