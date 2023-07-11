package com.example.UploadFile.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {

    public void uploadFile( MultipartFile file) throws IllegalStateException, IOException {
       file.transferTo(new File("C:\\Users\\User\\Desktop\\uploaded files" + file.getOriginalFilename()));
    }
}
