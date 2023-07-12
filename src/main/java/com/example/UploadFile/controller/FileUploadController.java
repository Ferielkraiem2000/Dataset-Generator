package com.example.UploadFile.controller;

import com.example.UploadFile.service.FileUploadService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponse(responseCode = "200", description = "File uploaded successfully")
    public void uploadFile(
            @Parameter( required = true)
            @RequestParam MultipartFile file
    ) throws IllegalStateException, IOException {

        fileUploadService.uploadFile(file);
    }
}
