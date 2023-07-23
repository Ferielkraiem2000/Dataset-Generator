package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.model.HistogramData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileParseService {


    ResponseEntity<List<String>> extractValidLines(MultipartFile file) throws IOException;

    ResponseEntity<List<List<String>>> extractLineContent(MultipartFile file) throws IOException;

    ResponseEntity<String> extractFileName(List<String> fields) throws IOException;

    ResponseEntity<List<Map<String, String>>> extractFields(MultipartFile file) throws IOException;

    ResponseEntity<Map<String, Object>> getDetailsForEachFile(Long file_id);
    public ResponseEntity<HistogramData> getHistogramData(Long file_id);

}
