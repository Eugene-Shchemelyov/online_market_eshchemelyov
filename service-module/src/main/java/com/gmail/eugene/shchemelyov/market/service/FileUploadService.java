package com.gmail.eugene.shchemelyov.market.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    void uploadItems(MultipartFile file);
}
