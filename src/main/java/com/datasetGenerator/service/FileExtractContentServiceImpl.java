package com.datasetGenerator.service;


import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Service
public class FileExtractContentServiceImpl implements FileExtractContentService {
    public ResponseEntity<String> readFileContent(MultipartFile file) throws IOException {

        InputStream inputStream = file.getInputStream();
        String fileContent = IOUtils.toString(inputStream, "UTF-8");
        inputStream.close();

        return ResponseEntity.ok(fileContent);
    }
}
