package com.datasetGenerator.annotationtool.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExtractSpecificDataService {
    public ResponseEntity <String>extractSpecificData(MultipartFile file) throws IOException;
}
