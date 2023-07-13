package com.datasetGenerator.controller;


import com.datasetGenerator.service.ExtractSpecificDataService;
import com.datasetGenerator.service.FileExtractContentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/extract-file-content")
@RestController
public class FileReadController {

    private final FileExtractContentService fileService;
    private final ExtractSpecificDataService dataService;

    public FileReadController(FileExtractContentService fileService, ExtractSpecificDataService dataService) {
        this.fileService = fileService;
        this.dataService = dataService;
    }

    //    @ApiResponse(responseCode = "200", description = "File content retrieved successfully")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponse(responseCode = "200", description = "File's Content extracted successfully")
    public ResponseEntity<String> readFile(@RequestParam("file") MultipartFile file) throws IOException {

//        String fileContent = String.valueOf(fileService.readFileContent(file));
//        String extractedData = String.valueOf(dataService.extractSpecificData(file));
        return ResponseEntity.ok(String.valueOf(dataService.extractSpecificData(file)));
    }


}
