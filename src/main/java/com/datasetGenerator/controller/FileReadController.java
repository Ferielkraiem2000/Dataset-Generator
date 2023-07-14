package com.datasetGenerator.controller;


import com.datasetGenerator.service.FileParseService;
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

@RequestMapping("/file-parsing")
@RestController
public class FileReadController {

    private final FileExtractContentService fileService;
    private final FileParseService dataService;

    public FileReadController(FileExtractContentService fileService, FileParseService dataService) {
        this.fileService = fileService;
        this.dataService = dataService;
    }

    //    @ApiResponse(responseCode = "200", description = "File content retrieved successfully")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponse(responseCode = "200", description = "File contents extracted successfully !")
    public ResponseEntity<String> readFile(@RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok(String.valueOf(dataService.extractFields(file)));
    }


}
