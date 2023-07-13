package com.datasetGenerator.annotationtool.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileExtractContentService {
//    public void uploadFile( MultipartFile file) throws IllegalStateException,IOException;
public ResponseEntity<String> readFileContent(MultipartFile file) throws IOException;
}
