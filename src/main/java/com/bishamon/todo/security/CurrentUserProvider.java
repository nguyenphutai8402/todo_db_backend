package com.bishamon.todo.security;

import com.bishamon.todo.entity.User;

public interface CurrentUserProvider {
    User getCurrentUser();
    Long getCurrentUserId();
}
