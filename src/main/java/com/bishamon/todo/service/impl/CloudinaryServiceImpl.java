package com.bishamon.todo.service.impl;

import com.bishamon.todo.config.properties.CloudinaryProperties;
import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {
    Cloudinary cloudinary;
    CloudinaryProperties cloudinaryProperties;

    static final long MAX_FILE_SIZE_MB = 5;
    static final long MAX_FILE_SIZE = MAX_FILE_SIZE_MB * 1024 * 1024;

    public static final List<String> ALLOWED_IMAGE_FILE = List.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    @Override
    public String uploadWorkspaceLogo(MultipartFile file, Long workspaceId) {
        validateImageFile(file);
        String assetFolder = cloudinaryProperties.getFolder().get("workspace-logo");
        String publicId =  "workspace-" + workspaceId;
        return uploadImage(file, assetFolder, publicId, 64);
    }

    @Override
    public void deleteWorkspaceLogo(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Deleted image from Cloudinary, publicId={}", publicId);
        } catch (AppException e) {
            throw e;
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    public void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new AppException(ErrorCode.FILE_EMPTY);
        if (!ALLOWED_IMAGE_FILE.contains(file.getContentType())) throw new AppException(ErrorCode.FILE_INVALID_TYPE);
        if (file.getSize() > MAX_FILE_SIZE) throw new AppException(ErrorCode.FILE_SIZE_EXCEEDED);
    }

    public String uploadImage(MultipartFile file, String assetFolder, String publicId, int dimensions) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", assetFolder,
                            "public_id", publicId,
                            "overwrite", true,
                            "invalidate", true,
                            "resource_type", "image",
                            "transformation", new Transformation()
                                    .width(dimensions)
                                    .height(dimensions)
                                    .crop("fill")
                                    .quality("auto")
                                    .fetchFormat("auto")
                    )
            );
            String secureUrl = (String) result.get("secure_url");
            log.info("Uploaded image to Cloudinary, url={}", secureUrl);
            return secureUrl;
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private String extractPublicId(String imageUrl) {
        try {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) throw new AppException(ErrorCode.FILE_INVALID_URL);

            String afterUpload = parts[1];
            if (afterUpload.matches("v\\d+/.*")) {
                afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
            }

            int dotIndex = afterUpload.lastIndexOf(".");
            if (dotIndex != -1) {
                afterUpload = afterUpload.substring(0, dotIndex);
            }

            return afterUpload;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to extract publicId from url: {}", imageUrl, e);
            throw new AppException(ErrorCode.FILE_INVALID_URL);
        }
    }
}
