package com.datasetgenerator.annotationtool.controller;

import com.datasetgenerator.annotationtool.service.FileExtractContentService;
import com.datasetgenerator.annotationtool.service.FileParseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/file-parsing")
@RestController
public class FileReadController {

    private final FileParseService dataService;
    private final FileExtractContentService fileService;

    public FileReadController(FileParseService dataService, FileExtractContentService fileService) {

        this.dataService = dataService;
        this.fileService = fileService;

    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> readFile(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<String> results = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!fileService.verifyType(file)) {
                return ResponseEntity.badRequest().body("File extension not allowed. Only '.txt' and '.stm' files are accepted!");
            }
            results.add(String.valueOf(dataService.extractFields(file)));
        }
        return ResponseEntity.ok(String.valueOf(results));
    }
}
