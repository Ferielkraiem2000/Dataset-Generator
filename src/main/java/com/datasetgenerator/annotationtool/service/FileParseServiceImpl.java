package com.datasetgenerator.annotationtool.service;


import com.datasetgenerator.annotationtool.model.File;
import com.datasetgenerator.annotationtool.repository.FileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class FileParseServiceImpl implements FileParseService {
    private final FileExtractContentService fileExtractContentService;
    private final FileRepository fileRepository;

    public FileParseServiceImpl(FileExtractContentService fileExtractContentService, FileRepository fileRepository) {
        this.fileExtractContentService = fileExtractContentService;
        this.fileRepository = fileRepository;
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

public ResponseEntity<String> extractFileName(Map<String, String> fields) throws IOException {
    List<String> extensions = Arrays.asList(".wav", ".mp3");
    String nameOfFile = "";

    for (Map.Entry<String, String> entry : fields.entrySet()) {
        String value = entry.getValue();
        for (String extension : extensions) {
            if (value.endsWith(extension)) {
                int extensionIndex = value.lastIndexOf(extension);
                nameOfFile = value.substring(0, extensionIndex);
                break;
            }
        }
    }
    return ResponseEntity.ok(nameOfFile);
}
    public ResponseEntity<List<Map<String, String>>> extractLineContent(MultipartFile file) throws IOException {

        ResponseEntity<List<String>> responseEntity = extractValidLines(file);
        List<String> validLines = responseEntity.getBody();
        List<Map<String, String>> lineContent = new ArrayList<>();
        assert validLines != null;
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
        for (int i = 0; i < Objects.requireNonNull(validLines).size(); i++) {
            assert lineContent != null;
            Map<String, String> fields = lineContent.get(i);
            Map<String, String> outputLine = new LinkedHashMap<>();
            String fileName=extractFileName(fields).getBody();
            outputLine.put("fileName", fileName);
            outputLine.put("speakerId", fields.get("field2"));
            outputLine.put("segmentStart", fields.get("field3"));
            outputLine.put("segmentEnd", fields.get("field4"));
            StringBuilder transcription =new StringBuilder();
            boolean afterField5 = false;

            for (Map.Entry<String, String> entry : fields.entrySet()) {
                if (afterField5) {
                    transcription.append(entry.getValue()).append(" ");
                }
                if (entry.getKey().equals("field5")) {
                    afterField5 = true;
                }
            }
            outputLine.put("transcription", String.valueOf(transcription));
            outputLines.add(outputLine);
            File fileEntity = new File();
            fileEntity.setFileName(fileName);
            fileEntity.setSpeakerId(fields.get("field2"));
            fileEntity.setSegmentStart(fields.get("field3"));
            fileEntity.setSegmentEnd(fields.get("field4"));
            fileEntity.setTranscription(transcription.toString().trim());
            fileRepository.save(fileEntity);

        }

        return ResponseEntity.ok(outputLines);
    }

}

