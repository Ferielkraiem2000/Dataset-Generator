package com.datasetGenerator.annotationtool.service;


import com.datasetGenerator.annotationtool.repository.FileExtractContentService;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Service
public class FileExtractContentServiceImpl implements FileExtractContentService {

    public ResponseEntity<String> readFileContent(MultipartFile file) throws IOException {

            // Get the input stream from the uploaded file
            InputStream inputStream = file.getInputStream();

            // Read the file content as a string
            String fileContent = IOUtils.toString(inputStream, "UTF-8");

            // Close the input stream
            inputStream.close();


        // Print the file content to the console
           // System.out.println(fileContent);
            // Return the file content as the response
            return  ResponseEntity.ok(fileContent);

    }

}
