package com.example.recyclingplastic.services.imp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.recyclingplastic.services.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor

public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;
    @Override
    public String upload(MultipartFile image) {
        try{
            Map<?, ?> response = cloudinary.uploader()
                    .upload(image.getBytes(), ObjectUtils.emptyMap());
            return response.get("url").toString();
        }catch (IOException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }
}
