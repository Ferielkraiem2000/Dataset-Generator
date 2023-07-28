package com.datasetgenerator.annotationtool.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UpdateUploadedFileService {
   void updateFile(MultipartFile file) throws IOException ;

    }
