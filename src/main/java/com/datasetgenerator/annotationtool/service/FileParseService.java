package com.datasetgenerator.annotationtool.service;

import org.springframework.web.multipart.MultipartFile;

import com.datasetgenerator.annotationtool.model.File;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileParseService {

  List<String> extractValidLines(MultipartFile file) throws IOException;

  List<List<String>> extractLineContent(MultipartFile file) throws IOException;

  String extractFileName(List<String> fields) throws IOException;

  String extractTranscription(List<String> fields);

  void uploadFile(MultipartFile file) throws IOException;

  void overwriteExistingFile(MultipartFile file) throws IOException;

  List<Map<String, String>> showFileContent(Long fileId);

}
