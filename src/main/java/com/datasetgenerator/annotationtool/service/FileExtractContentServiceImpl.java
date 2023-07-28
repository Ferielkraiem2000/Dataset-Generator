package com.datasetgenerator.annotationtool.service;


import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Service
public class FileExtractContentServiceImpl implements FileExtractContentService {
   public boolean verifyType(MultipartFile file){
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        if (!fileExtension.equalsIgnoreCase("txt") && !fileExtension.equalsIgnoreCase("stm")) {
            return false;
        }
        return true;
    }
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? null : fileName.substring(dotIndex + 1);
    }

    public ResponseEntity<String> readFileContent(MultipartFile file) throws IOException {

        InputStream inputStream = file.getInputStream();
        String fileContent = IOUtils.toString(inputStream, "UTF-8");
        inputStream.close();
        return ResponseEntity.ok(fileContent);
    }
}
