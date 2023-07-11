package com.example.UploadFile.controller;

import com.example.UploadFile.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {

    FileUploadService fileUploadService;
    @Autowired
    FileUploadController(FileUploadService fileUploadService){
     this.fileUploadService=fileUploadService;
    }

    @PostMapping
     public void uploadFile(@RequestParam("file")MultipartFile file) throws IllegalStateException, IOException {
       fileUploadService.uploadFile(file);
     }
}


