package com.bishamon.todo.service;

public interface ImageService {
    String generateImageUrl(String name);
    String getImageUrl(String imageUrl, String name);
}
