package com.ems.userservice.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageServiceInterface {

    String uploadFile(MultipartFile file) throws IOException;

}