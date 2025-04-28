package com.example.borrowit.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageUploadService {
    List<byte[]> saveImages(MultipartFile[] files) throws IOException;  // Changed to return List<byte[]>
    List<byte[]> saveImagesAsBytes(MultipartFile[] images) throws IOException;  // Re-added this method
}
