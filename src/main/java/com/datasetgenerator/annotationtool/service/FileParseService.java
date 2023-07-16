package com.datasetgenerator.annotationtool.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileParseService {


 public ResponseEntity<List<String>> extractValidLines(MultipartFile file) throws IOException;
ResponseEntity<String> extractFileName(Map<String,String>fields) throws IOException;
 public ResponseEntity<List<Map<String, String>>>extractLineContent(MultipartFile file) throws IOException;

 public ResponseEntity<List<Map<String, String>>> extractFields(MultipartFile file) throws IOException;

}
