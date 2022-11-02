package com.ems.userservice.service.implementations;

import com.ems.userservice.exceptions.StorageException;
import com.ems.userservice.service.StorageServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService implements StorageServiceInterface{

    @Value("${storage.upload.path}")
    private String path;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new StorageException("Failed to upload empty file");
        } else if(!file.getContentType().equalsIgnoreCase("text/csv")){
            throw new StorageException("Invalid file type");
        }
        String destinationPath = "";
        try {
            String fileName = file.getOriginalFilename();
            InputStream sourceInputStream = file.getInputStream();
            destinationPath = path+fileName;
            System.out.println(Files.copy(sourceInputStream, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING));

        } catch (IOException e) {
            throw new StorageException(String.format("Failed to store file %f", file.getName()));
        }
        return destinationPath;
    }
}

