package com.datasetGenerator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class FileParseServiceImpl implements FileParseService {
    private final FileExtractContentService fileExtractContentService;
    private static final Logger log = LoggerFactory.getLogger(FileParseServiceImpl.class);

    public FileParseServiceImpl(FileExtractContentService fileExtractContentService) {
        this.fileExtractContentService = fileExtractContentService;
    }

    public ResponseEntity<List<String>> extractValidLines(MultipartFile file) throws IOException {
        ResponseEntity<String> response = fileExtractContentService.readFileContent(file);
        String fileContent = response.getBody();
        String minimumFieldsEnv = System.getProperty("MINIMUM_FIELDS");
        int minimumFields = 7;
        if (minimumFieldsEnv != null) {
            try {
                minimumFields = Integer.parseInt(minimumFieldsEnv);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        List<String> validLines = new ArrayList<>();
        String[] lines = fileContent.split("\n");
        for (String line : lines) {
            String[] fields = line.split(" ");
            if (fields.length >= minimumFields && !line.startsWith(";;")) {
                validLines.add(line);
            }
        }
        return ResponseEntity.ok().body(validLines);
    }

    public ResponseEntity<List<Map<String, String>>> extractLineContent(MultipartFile file) throws IOException {
        ResponseEntity<List<String>> responseEntity = extractValidLines(file);
        List<String> validLines = responseEntity.getBody();
        List<Map<String, String>> lineContent = new ArrayList<>();

        for (String line : validLines) {
            String[] fields = line.split(" ");
            Map<String, String> currentLineContent = new LinkedHashMap<>();

            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].equals("1")) {
                    currentLineContent.put("field" + i, fields[i]);
                }
            }

            lineContent.add(currentLineContent);
        }

        return ResponseEntity.ok(lineContent);
    }





    public ResponseEntity<List<Map<String, String>>> extractFields(MultipartFile file) throws IOException {

        List<String> validLines = extractValidLines(file).getBody();
        ResponseEntity<List<Map<String, String>>> lineContentResponse = extractLineContent(file);
        List<Map<String, String>> lineContent = lineContentResponse.getBody();
        List<Map<String, String>> outputLines = new ArrayList<>();

        for (int i = 0; i < validLines.size(); i++) {
            Map<String, String> fields = lineContent.get(i);
            Map<String, String> outputLine = new LinkedHashMap<>();
            outputLine.put("fileName", fields.get("field0"));
            outputLine.put("speakerId", fields.get("field2"));
            outputLine.put("segmentStart", fields.get("field3"));
            outputLine.put("segmentEnd", fields.get("field4"));
            String transcription = "";
            boolean afterField5 = false;

            for (Map.Entry<String, String> entry : fields.entrySet()) {
                if (afterField5) {
                    transcription += entry.getValue() + " ";
                }
                if (entry.getKey().equals("field5")) {
                    afterField5 = true;
                }
            }

            outputLine.put("transcription", transcription.trim());
            outputLines.add(outputLine);

        }
        log.debug(outputLines.toString());
        return ResponseEntity.ok(outputLines);
    }
}
