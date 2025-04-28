package com.example.borrowit.Service.Impl;

import com.example.borrowit.Service.ImageUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    // This method saves images as byte arrays
    @Override
    public List<byte[]> saveImages(MultipartFile[] files) throws IOException {
        List<byte[]> imageBytesList = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                imageBytesList.add(bytes);  // Store image bytes
            }
        }

        return imageBytesList;
    }

    // This method saves images as byte arrays (same logic as saveImages)
    @Override
    public List<byte[]> saveImagesAsBytes(MultipartFile[] images) throws IOException {
        return saveImages(images);  // Directly reuse the saveImages method
    }
}
