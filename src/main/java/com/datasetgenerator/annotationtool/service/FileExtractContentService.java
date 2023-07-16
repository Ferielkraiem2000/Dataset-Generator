package com.datasetgenerator.annotationtool.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileExtractContentService {
   ResponseEntity<String> readFileContent(MultipartFile file) throws IOException;
}
