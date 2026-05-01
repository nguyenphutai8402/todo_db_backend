package com.bishamon.todo.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadWorkspaceLogo(MultipartFile file, Long workspaceId);
    void deleteWorkspaceLogo(String url);
}
