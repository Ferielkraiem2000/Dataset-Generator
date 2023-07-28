package com.datasetgenerator.annotationtool.controller;

import com.datasetgenerator.annotationtool.service.*;
import com.datasetgenerator.annotationtool.util.HistogramData;
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

@RestController
public class FileReadController {

    private final FileParseService dataService;
    private final FileExtractContentService fileService;
    private final DownloadManifestFileService downloadManifestFileService;
    private final UpdateUploadedFileService updateUploadedFileService;
    private final StatisticsService statisticsService;

    public FileReadController(FileParseService dataService, FileExtractContentService fileService, DownloadManifestFileService downloadManifestFileService, UpdateUploadedFileService updateUploadedFileService, StatisticsService statisticsService) {

        this.dataService = dataService;
        this.fileService = fileService;
        this.downloadManifestFileService = downloadManifestFileService;
        this.updateUploadedFileService = updateUploadedFileService;
        this.statisticsService = statisticsService;
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

    @GetMapping("/file-parsing")
    public ResponseEntity<?> downloadManifestFile(
            @RequestParam(name = "format", required = true) String format,
            @RequestParam(name = "file_id") List<String> fileIds,
            @RequestParam(name = "path", required = false) String path) throws IOException {

        if (format.equalsIgnoreCase("JSON")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=combined_manifest.json");
            String combinedManifestJson = downloadManifestFileService.createCombinedManifestInJsonFormat(path, fileIds);
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(combinedManifestJson);

        }

        if (format.equalsIgnoreCase("CSV")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=combined_manifest.csv");
            String combinedManifestCsv = downloadManifestFileService.createCombinedManifestInCsvFormat(path, fileIds);
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(combinedManifestCsv);

        }

        if (format.equalsIgnoreCase("ESPnet")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=FileForESPnet.zip");
            ByteArrayResource combinedManifestESPnet = downloadManifestFileService.createFileCompatibleWithESPnet(path, fileIds);
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(combinedManifestESPnet);
        }
        return ResponseEntity.badRequest().body("Format :" + format + " not supported!");
    }
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,path="/file-parsing")
    public void updateFile(@RequestParam("file") MultipartFile file) throws IOException {
        updateUploadedFileService.updateFile(file);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics(@RequestParam(name = "file_id", required = false) List<Long> fileIds) {

      if(fileIds!=null){
            return statisticsService.getStatisticsForEachFile(fileIds);
        }
      return statisticsService.getStatistics();
    }

    @GetMapping("/histogram")
    public ResponseEntity<HistogramData> getHistogramData() {
        return statisticsService.getHistogramData();
    }

}
