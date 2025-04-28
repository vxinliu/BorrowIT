package com.example.borrowit.service.impl;
import com.cloudinary.Cloudinary;
import com.example.borrowit.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
@Service
public class FileUploadImpl implements FileUploadService {

    private final Cloudinary cloudinary;
    public FileUploadImpl(Cloudinary cloudinary){
        this.cloudinary=cloudinary;
    }
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }
}