package com.datasetGenerator.annotationtool.controller;


import com.datasetGenerator.annotationtool.repository.ExtractSpecificDataService;
import com.datasetGenerator.annotationtool.repository.FileExtractContentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileReadController {

    private final FileExtractContentService fileService;
    private final ExtractSpecificDataService dataService;

    public FileReadController(FileExtractContentService fileService,ExtractSpecificDataService dataService) {
        this.fileService = fileService;
        this.dataService=dataService;
    }

    @ApiResponse(responseCode = "200", description = "File content retrieved successfully")
    @GetMapping("/extract-file-content")
    public ResponseEntity<String> readFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Call the file service to read the file content
        String fileContent = String.valueOf(fileService.readFileContent(file));
        String extractedData = String.valueOf(dataService.extractSpecificData(file));
        // Return the file content as the response
        return ResponseEntity.ok(extractedData);
    }


}
