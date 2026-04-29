package com.bishamon.todo.service.impl;

import com.bishamon.todo.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ImageServiceImpl implements ImageService {
    private static final String UI_IMAGE_BASE_URL = "https://ui-avatars.com/api/";

    @Override
    public String generateImageUrl(String name){
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        return UriComponentsBuilder.fromPath(UI_IMAGE_BASE_URL)
                .queryParam("name", encodedName)
                .queryParam("background", "random")
                .queryParam("color", "fff")
                .queryParam("bold", "true")
                .toUriString();
    }

    @Override
    public String getImageUrl(String imageUrl, String name) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return generateImageUrl(name);
        }
        return imageUrl;
    }
}
