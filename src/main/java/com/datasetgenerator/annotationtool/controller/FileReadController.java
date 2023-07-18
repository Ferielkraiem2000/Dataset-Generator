package com.datasetgenerator.annotationtool.controller;
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
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/file-parsing")
@RestController
public class FileReadController {
    private final FileParseService dataService;
    public FileReadController(FileParseService dataService) {

        this.dataService = dataService;
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "File Parsing")
    public ResponseEntity<String> readFile(@RequestParam("files") List<MultipartFile> files) throws IOException {
        if (files.isEmpty()) {
            return ResponseEntity.badRequest().body("No files selected.");
        }
        List<String> results = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);
            if (!fileExtension.equalsIgnoreCase("txt") && !fileExtension.equalsIgnoreCase("stm")) {
                return ResponseEntity.badRequest().body("File extension not allowed. Only '.txt' and '.stm' files are accepted!");
            }
            results.add(String.valueOf(dataService.extractFields(file)));
        }
        return ResponseEntity.ok(String.valueOf(results));
    }
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? null : fileName.substring(dotIndex + 1);
    }
}
