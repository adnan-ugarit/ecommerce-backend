package com.adnan.catalog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class FileUtil {
    
    public static String storeFile(String uploadDir, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String[] fields = fileName.split("\\.");
        String extension = fields[fields.length - 1];
        if ((!"jpg".equals(extension)) && (!"jpeg".equals(extension)) && (!"png".equals(extension))) {
            throw new IOException("File extension is not supported");
        }
        Path uploadPath = Paths.get(uploadDir);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            return baseUrl + "/" + uploadDir + "/" + fileName;
        }
        catch (IOException io) {
            throw new IOException("Failed to store image file: " + fileName, io);
        }
    }
    
    public static Resource loadFile(String downloadDir, String fileName) {
        Path downloadPath = Paths.get(downloadDir);
        try {
            Path filePath = downloadPath.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            else {
                throw new RuntimeException("File (name = " + fileName + ") is not found");
            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    
}
