package com.bishamon.todo.config;

import com.bishamon.todo.config.properties.CloudinaryProperties;
import com.cloudinary.Cloudinary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryConfig {
    CloudinaryProperties cloudinaryProperties;

    @Bean
    public Cloudinary cloudinary(){

        return new Cloudinary(Map.of(
                "cloud_name", cloudinaryProperties.getCloudName(),
                "api_key", cloudinaryProperties.getApiKey(),
                "api_secret", cloudinaryProperties.getApiSecret()
        ));
    }
}
