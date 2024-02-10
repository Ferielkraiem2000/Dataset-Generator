package com.datasetgenerator.annotationtool.controller;

import com.datasetgenerator.annotationtool.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Updating controller to view a build triggered automatically on Jenkins
@RestController
public class ReadFileController {

    private final FileParseService dataService;
    private final ExtractFileContentsService fileService;
    private final DownloadManifestFileService downloadManifestFileService;
    private final UpdateUploadedFileService updateUploadedFileService;
    private final StatisticsService statisticsService;
    private final DeleteService deleteService;

    public ReadFileController(FileParseService dataService, ExtractFileContentsService fileService, DownloadManifestFileService downloadManifestFileService, UpdateUploadedFileService updateUploadedFileService, StatisticsService statisticsService, DeleteService deleteService) {
        this.dataService = dataService;
        this.fileService = fileService;
        this.downloadManifestFileService = downloadManifestFileService;
        this.updateUploadedFileService = updateUploadedFileService;
        this.statisticsService = statisticsService;
        this.deleteService = deleteService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/file-parsing")
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

    @Operation(summary = "Download Manifest File")
    @GetMapping(path = "/file-parsing")
    public ResponseEntity<?> downloadManifestFile(@RequestParam(name = "format", required = true) String format, @RequestParam(name = "file_id") List<Long> fileIds, @RequestParam(name = "path") String path) throws IOException {

        ByteArrayResource combinedManifest = downloadManifestFileService.createCombinedManifest(format, path, fileIds);
        HttpHeaders headers = new HttpHeaders();
        if (format.equalsIgnoreCase("JSON")) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=combined_manifest.json");
        } else if (format.equalsIgnoreCase("CSV")) {
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=combined_manifest.csv");
        } else if (format.equalsIgnoreCase("ESPnet")) {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fileForESPnet.zip");
        }
        return ResponseEntity.ok().headers(headers).body(combinedManifest);
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<Map<String, Object>>> getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @PutMapping(path = "/file-parsing")
    public ResponseEntity<String> updateFileName(@RequestParam("fileId") Long fileId, @RequestParam("fileName") String fileName) {
        try {
            updateUploadedFileService.updateFileName(fileId, fileName);
            return ResponseEntity.ok("fileName updated successfully!");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body("fileId doesn't exist!");

        }
    }

    @DeleteMapping("/file-parsing")
    public ResponseEntity<String> deleteFiles(@RequestParam List<Long> fileIds) {
        deleteService.deleteSegmentsByFileIds(fileIds);
        return ResponseEntity.ok("Files deleted successfully.");
    }
}
