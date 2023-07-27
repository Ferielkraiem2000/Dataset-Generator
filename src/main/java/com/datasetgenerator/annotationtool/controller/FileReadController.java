package com.datasetgenerator.annotationtool.controller;

import com.datasetgenerator.annotationtool.service.DownloadManifestFileService;
import com.datasetgenerator.annotationtool.service.FileExtractContentService;
import com.datasetgenerator.annotationtool.service.FileParseService;
import com.datasetgenerator.annotationtool.service.StatisticsService;
import com.datasetgenerator.annotationtool.util.HistogramData;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    private final StatisticsService statisticsService;

    public FileReadController(FileParseService dataService, FileExtractContentService fileService, DownloadManifestFileService downloadManifestFileService, StatisticsService statisticsService) {

        this.dataService = dataService;
        this.fileService = fileService;
        this.downloadManifestFileService = downloadManifestFileService;
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

    @GetMapping("/get-manifest-file")
    public ResponseEntity<?> getManifestFile(
            @RequestParam(name = "format", required = true) String format,
            @RequestParam(name = "file_id") List<String> fileIds,
            @RequestParam(name = "path", required = false) String path) throws IOException {

        if (format.equalsIgnoreCase("Json")) {
            return downloadManifestFileService.downloadCombinedManifestInJsonFormat(path,fileIds);
        }

        if (format.equalsIgnoreCase("Csv")) {
            return downloadManifestFileService.downloadCombinedManifestInCsvFormat(path,fileIds);
        }

        if (format.equalsIgnoreCase("ESPnet")) {
            return downloadManifestFileService.downloadFileCompatibleWithESPnet(path, fileIds);
        }

        return ResponseEntity.badRequest().body("Format :" + format+ " not supported!");
    }

    @GetMapping("/dataset-statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return statisticsService.getStatistics();
    }
    @GetMapping("/file-statistics")
    public ResponseEntity<Map<String, Object>> getStatisticsForEachFile(@RequestParam(name="file_id") List<Long> fileIds) {
        return statisticsService.getStatisticsForEachFile(fileIds);
    }

    @GetMapping("get-histogram-data")
    public ResponseEntity<HistogramData> getHistogramData() {
        return statisticsService.getHistogramData();
    }

}
