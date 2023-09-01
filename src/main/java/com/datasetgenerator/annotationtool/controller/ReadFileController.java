package com.datasetgenerator.annotationtool.controller;

import com.datasetgenerator.annotationtool.model.File;
import com.datasetgenerator.annotationtool.service.*;
import com.datasetgenerator.annotationtool.util.HistogramData;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class ReadFileController {

    private final FileParseService dataService;
    private final ExtractFileContentsService fileService;
    private final DownloadManifestFileService downloadManifestFileService;
    private final UpdateUploadedFileService updateUploadedFileService;
    private final StatisticsService statisticsService;
    private final DeleteService deleteService;

    public ReadFileController(FileParseService dataService, ExtractFileContentsService fileService,
            DownloadManifestFileService downloadManifestFileService,
            UpdateUploadedFileService updateUploadedFileService, StatisticsService statisticsService,
            DeleteService deleteService) {
        this.dataService = dataService;
        this.fileService = fileService;
        this.downloadManifestFileService = downloadManifestFileService;
        this.updateUploadedFileService = updateUploadedFileService;
        this.statisticsService = statisticsService;
        this.deleteService = deleteService;
    }

    @Operation(summary = "Parse File")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/file-parsing")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files)
            throws IOException {
        String result = "";
        for (MultipartFile file : files) {
            if (!fileService.verifyType(file)) {
                return ResponseEntity.badRequest()
                        .body("File extension not allowed. Only '.txt' and '.stm' files are accepted!");
            }
            File existingFile = dataService.existsFile(file);
            if (existingFile == null) {
                dataService.uploadFile(file, false, existingFile);
                result = "File Uploaded Successfully!";
            } else {
                result = "file exists";

            }
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Overwrite File")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/file-overwriting")
    public ResponseEntity<String> overwriteFiles(@RequestParam("files") List<MultipartFile> files)
            throws IOException {
        String result = "";
        for (MultipartFile file : files) {
            if (!fileService.verifyType(file)) {
                return ResponseEntity.badRequest()
                        .body("File extension not allowed. Only '.txt' and '.stm' files are accepted!");
            }
            File existingFile = dataService.existsFile(file);
            if (existingFile != null) {
                dataService.uploadFile(file, true, existingFile);
                result = "File Overwritten Successfully!";
            }
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get File Content ")
    @GetMapping(path = "/file-parsing/{id}")
    public ResponseEntity<List<Map<String, String>>> showContent(@RequestParam("fileId") Long fileId) {
        return ResponseEntity.ok(dataService.showFileContent(fileId));
    }

    @Operation(summary = "Download Manifest File")
    @GetMapping(path = "/file-parsing/{format}/{ids}/{path}")
    public ResponseEntity<?> downloadManifestFile(@RequestParam(name = "format", required = true) String format,
            @RequestParam(name = "file_id") List<Long> fileIds,
            @RequestParam(name = "path", required = false) String path) throws IOException {

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

    @Operation(summary = "Get Dataset statistics")
    @GetMapping("/dataset/statistics")
    public ResponseEntity<List<Map<String, Object>>> getDatasetStatistics() {
        return ResponseEntity.ok(statisticsService.getDatasetStatistics());
    }

    @Operation(summary = "Get Files statistics")
    @GetMapping("/files/statistics")
    public ResponseEntity<List<Map<String, Object>>> getFilesStatistics() {
        return ResponseEntity.ok(statisticsService.getFilesStatistics());
    }

    @Operation(summary = "Get File statistics By File Name")
    @GetMapping("/files/statistics/{name}")
    public ResponseEntity<List<Map<String, Object>>> getFilesStatistics(String fileName) {
        return ResponseEntity.ok(statisticsService.getFilesStatisticsByFileName(fileName));
    }

    @Operation(summary = "Get the statistics of one or more selected uploaded STM files")
    @GetMapping("/files/statistics/{ids}")
    public ResponseEntity<List<Map<String, Object>>> getFilesStatistics(@RequestParam("fileIds") List<Long> fileIds) {
        return ResponseEntity.ok(statisticsService.getFilesStatistics(fileIds));
    }

    @Operation(summary = "Update File name")
    @PutMapping(path = "/file-parsing/{id}/{name}")
    public ResponseEntity<String> updateFileName(@RequestParam("fileId") Long fileId,
            @RequestParam("fileName") String fileName) {
        updateUploadedFileService.updateFileName(fileId, fileName);
        if (fileName.equals("")) {
            return ResponseEntity.badRequest().body("Error updating Audio File Name!");
        }
        return ResponseEntity.ok("fileName updated successfully!");

    }

    @Operation(summary = "Delete file")
    @DeleteMapping("/file-parsing/{ids}")
    public ResponseEntity<String> deleteFiles(@RequestParam List<Long> fileIds) {
        deleteService.deleteByFileIds(fileIds);
        return ResponseEntity.ok("Files deleted successfully!");
    }

    @Operation(summary = "Get the histogram data of all uploaded STM files")
    @GetMapping("/histogram/{size}")
    public ResponseEntity<HistogramData> getHistogramData(@RequestParam("size") double intervalSize) {
        HistogramData histogramData = statisticsService.getHistogramData(intervalSize);
        return ResponseEntity.ok(histogramData);
    }

    @Operation(summary = "Get the histogram data of one or more selected uploaded STM files")
    @GetMapping("/histogram/{ids}/{size}")
    public ResponseEntity<HistogramData> getHistogramData(@RequestParam("fileIds") List<Long> fileIds,
            @RequestParam("intervalSize") double intervalSize) {
        HistogramData histogramData = statisticsService.getHistogramDataForSelectedFiles(fileIds, intervalSize);
        return ResponseEntity.ok(histogramData);
    }
}
