package com.example.recyclingplastic.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String upload(MultipartFile image);
}
