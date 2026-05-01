package com.bishamon.todo.config.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryProperties {
    String cloudName;
    String apiKey;
    String apiSecret;
    Map<String, String> folder;
}
