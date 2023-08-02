package com.datasetgenerator.annotationtool.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileParseService {


    List<String> extractValidLines(MultipartFile file) throws IOException;

    List<List<String>> extractLineContent(MultipartFile file) throws IOException;

    String extractFileName(List<String> fields) throws IOException;

    StringBuilder extractTranscription(List<String> fields);

    List<Map<String, String>> extractFields(MultipartFile file,boolean overwrite) throws IOException;

}
