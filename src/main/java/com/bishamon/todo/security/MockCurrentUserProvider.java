package com.bishamon.todo.security;

import com.bishamon.todo.entity.User;
import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MockCurrentUserProvider implements CurrentUserProvider{
    private static final Long MOCK_USER_ID = 1L;

    @Override
    public Long getCurrentUserId() {
        return MOCK_USER_ID;
    }

}
