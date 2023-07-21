package com.datasetgenerator.annotationtool.service;


import com.datasetgenerator.annotationtool.model.File;
import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.FileRepository;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class FileParseServiceImpl implements FileParseService {

    private final FileExtractContentService fileExtractContentService;
    private final FileRepository fileRepository;
    private final SegmentRepository segmentRepository;
    @Value("${file.extensions}")
    private String fileExtensions;
    private int minimumFields;

    public FileParseServiceImpl(FileExtractContentService fileExtractContentService, FileRepository fileRepository, SegmentRepository segmentRepository, @Value("${minimumFields}") int minimumFields) {
        this.fileExtractContentService = fileExtractContentService;
        this.fileRepository = fileRepository;
        this.segmentRepository = segmentRepository;
        this.minimumFields = minimumFields;
    }

    public ResponseEntity<List<String>> extractValidLines(MultipartFile file) throws IOException {
        ResponseEntity<String> response = fileExtractContentService.readFileContent(file);
        String fileContent = response.getBody();
        List<String> validLines = new ArrayList<>();
        String[] lines = fileContent.split("\n");
        for (String line : lines) {
            String[] fields = line.split(" ");
            if (fields.length >= minimumFields && !line.startsWith(";;")) {
                validLines.add(line);
            }
        }
        System.out.println(validLines);
        return ResponseEntity.ok().body(validLines);
    }


    public ResponseEntity<List<List<String>>> extractLineContent(MultipartFile file) throws IOException {

        ResponseEntity<List<String>> responseEntity = extractValidLines(file);
        List<String> validLines = responseEntity.getBody();
        List<List<String>> lineContent = new ArrayList<>();
        assert validLines != null;
        for (String line : validLines) {
            String[] fields = line.split(" ");
            List<String> currentLineContent = new ArrayList<>();
            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].equals("1")) {
                    currentLineContent.add(fields[i]);
                }

            }
            lineContent.add(currentLineContent);
        }
        return ResponseEntity.ok(lineContent);
    }

    public ResponseEntity<String> extractFileName(List<String> fields) throws IOException {
        List<String> extensions = Arrays.asList(fileExtensions.split(","));
        String fileName = "";
        for (String field : fields) {
            for (String extension : extensions) {
                if (field.endsWith(extension)) {
                    int extensionIndex = field.lastIndexOf(extension);
                    fileName = field.substring(0, extensionIndex + extension.length());
                    break;
                }
            }
        }

        return ResponseEntity.ok(fileName);
    }

    public ResponseEntity<StringBuilder> extractTranscription(List<String> fields) {
        StringBuilder transcription = new StringBuilder();
        boolean afterField4 = false;
        for (String field : fields) {
            if (afterField4) {
                transcription.append(field).append(" ");
            }
            if (field.equals(fields.get(4))) {
                afterField4 = true;
            }
        }
        return ResponseEntity.ok(transcription);
    }

    public ResponseEntity<List<Map<String, String>>> extractFields(MultipartFile file) throws IOException {
        List<String> validLines = extractValidLines(file).getBody();
        ResponseEntity<List<List<String>>> lineContentResponse = extractLineContent(file);
        List<List<String>> lineContent = lineContentResponse.getBody();
        List<Map<String, String>> outputLines = new ArrayList<>();
        File fileEntity = new File();
        fileRepository.save(fileEntity);
        boolean fileExists = false;
        String fileName = "";
        for (int i = 0; i < Objects.requireNonNull(validLines).size(); i++) {
            assert lineContent != null;
            List<String> fields = lineContent.get(i);
            Map<String, String> outputLine = new LinkedHashMap<>();
            fileName = extractFileName(fields).getBody();
            outputLine.put("file_name", fileName);
            outputLine.put("speaker", fields.get(1));
            outputLine.put("segment_start", fields.get(2));
            outputLine.put("segment_end", fields.get(3));
            double segmentStart = Double.parseDouble(outputLine.get("segment_start"));
            double segmentEnd = Double.parseDouble(outputLine.get("segment_end"));
            double duration = segmentEnd - segmentStart;
            StringBuilder transcription = extractTranscription(fields).getBody();
            outputLine.put("transcription", String.valueOf(transcription));
            outputLines.add(outputLine);
            if (fileRepository.existsByFileName(fileName)) {
                fileExists = true;
                Map<String, String> errorResponse = new LinkedHashMap<>();
                errorResponse.put("error", "File already exists!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(errorResponse));
            }
            Segment segment = new Segment();
            segment.setSpeaker(outputLine.get("speaker"));
            segment.setSegment_start(Double.parseDouble(outputLine.get("segment_start")));
            segment.setSegment_end(Double.parseDouble(outputLine.get("segment_end")));
            segment.setDuration(duration);
            segment.setTranscription(outputLine.get("transcription"));
            segment.setFile(fileEntity);
            segment.setDuration(duration);
            segmentRepository.save(segment);
        }
        if (!fileExists) {
            fileEntity.setFile_name(fileName);
            fileRepository.save(fileEntity);
        }

        return ResponseEntity.ok(outputLines);
    }

    public ResponseEntity<Map<String, Object>> getDetailsForEachFile(Long file_id) {
        List<Object[]> result = segmentRepository.getDetailsForEachFile(file_id);
        Map<String, Object> fileDetails = new LinkedHashMap<>();
        for (Object[] row : result) {

            Long segmentCount = (Long) row[0];
            Double averageDuration = (Double) row[1];
            Double totalDuration = (Double) row[2];
            fileDetails.put("segmentCount", segmentCount);
            fileDetails.put("averageDuration", averageDuration);
            fileDetails.put("totalDuration", totalDuration);
        }
        return ResponseEntity.ok(fileDetails);
    }
}

