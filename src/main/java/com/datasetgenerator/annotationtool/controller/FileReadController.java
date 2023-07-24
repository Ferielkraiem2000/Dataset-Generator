package com.datasetgenerator.annotationtool.controller;

import com.datasetgenerator.annotationtool.model.HistogramData;
import com.datasetgenerator.annotationtool.repository.FileRepository;
import com.datasetgenerator.annotationtool.service.FileExtractContentService;
import com.datasetgenerator.annotationtool.service.FileParseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/file-parsing")
@RestController
public class FileReadController {

    private final FileParseService dataService;
    private final FileExtractContentService fileService;

    public FileReadController(FileParseService dataService, FileExtractContentService fileService, FileRepository fileRepository) {

        this.dataService = dataService;
        this.fileService = fileService;

    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "File Parsing")
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

    @GetMapping("/file/{id}")
    public ResponseEntity<String> getStatisticsForEachFile(@PathParam("file_id") Long file_id) {
        Map<String, Object> detailsFile = dataService.getDetailsForEachFile(file_id).getBody();
        return ResponseEntity.ok(detailsFile.toString());
    }

    @GetMapping("/histogram/file/{id}")
    public ResponseEntity<HistogramData> getHistogramData(@PathParam("file_id") Long file_id) {
        ResponseEntity<HistogramData> histogramDataResponseEntity = dataService.getHistogramData(file_id);
        return ResponseEntity.ok(histogramDataResponseEntity.getBody());
    }

    @GetMapping("/quartiles/{id}")
    @Operation(summary = "la distribution des durées des segments du fichier ")
    public ResponseEntity<List<Double>> calculateQuartiles(@PathParam("file_id") Long file_id) {
        ResponseEntity<List<Double>> quartiles = dataService.calculateQuartiles(file_id);
        return ResponseEntity.ok(quartiles.getBody());
    }
}
