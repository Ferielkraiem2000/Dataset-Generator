package com.datasetgenerator.annotationtool.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExtractFileContentsService {
    String readFileContent(MultipartFile file) throws IOException;
    boolean verifyType(MultipartFile file);
}
