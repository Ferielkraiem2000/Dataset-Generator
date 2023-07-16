package com.datasetgenerator.annotationtool.controller;


import com.datasetgenerator.annotationtool.service.FileExtractContentService;
import com.datasetgenerator.annotationtool.service.FileParseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/file_parsing")
@RestController
public class FileReadController {

    private final FileExtractContentService fileService;
    private final FileParseService dataService;

    public FileReadController(FileExtractContentService fileService, FileParseService dataService) {
        this.fileService = fileService;
        this.dataService = dataService;
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "File Parsing")
    public ResponseEntity<String> readFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(String.valueOf(dataService.extractFields(file)));
    }


}
