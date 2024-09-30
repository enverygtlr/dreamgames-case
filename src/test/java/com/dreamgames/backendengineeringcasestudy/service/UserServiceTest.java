package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.entity.User;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
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


}