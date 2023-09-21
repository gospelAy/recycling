package com.example.recyclingplastic.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CloudinaryServiceImplTest {
    @Autowired CloudinaryService cloudinaryService;

    @Test
    void upload() {
        MultipartFile image = uploadImage("C:\\Users\\ADMIN\\Downloads\\recycling-plastic\\src\\main\\resources\\static\\IMG-20230618-WA0009.jpg");
        String uploadedImageUrl = cloudinaryService.upload(image);
        assertThat(uploadedImageUrl).isNotNull();
    }

    private MultipartFile uploadImage(String imageUrl){
        MultipartFile file = null;
        try{
            file = new MockMultipartFile("image",
                    new FileInputStream(imageUrl));
        }catch(IOException exception){
            System.out.println(exception.getMessage());
        }
        return file;
    }
}