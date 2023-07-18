package com.datasetgenerator.annotationtool.service;


import com.datasetgenerator.annotationtool.model.File;
import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.FileRepository;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
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

    public FileParseServiceImpl(FileExtractContentService fileExtractContentService, FileRepository fileRepository, SegmentRepository segmentRepository) {
        this.fileExtractContentService = fileExtractContentService;
        this.fileRepository = fileRepository;
        this.segmentRepository = segmentRepository;
    }

    public ResponseEntity<List<String>> extractValidLines(MultipartFile file) throws IOException {
        ResponseEntity<String> response = fileExtractContentService.readFileContent(file);
        String fileContent = response.getBody();
        String minimumFieldsEnv = System.getenv("MINIMUM_FIELDS");
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


            String extensionsEnv = System.getenv("EXTENSIONS");
            List<String> extensions = Arrays.asList(".wav", ".mp3");
            if (extensionsEnv != null && !extensionsEnv.isEmpty()) {
                extensions = Arrays.asList(extensionsEnv.split(","));
            }
            String fileName = "";
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                String value = entry.getValue();
                for (String extension : extensions) {
                    if (value.endsWith(extension)) {
                        int extensionIndex = value.lastIndexOf(extension);
                        fileName = value.substring(0, extensionIndex+extension.length());
                        break;
                    }
                }
            }
            return ResponseEntity.ok(fileName);
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
        File fileEntity = new File();
        fileRepository.save(fileEntity);
        for (int i = 0; i < Objects.requireNonNull(validLines).size(); i++) {
            assert lineContent != null;
            Map<String, String> fields = lineContent.get(i);
            Map<String, String> outputLine = new LinkedHashMap<>();
            String fileName = extractFileName(fields).getBody();
            outputLine.put("file_name", fileName);
            outputLine.put("speaker", fields.get("field2"));
            outputLine.put("segment_start", fields.get("field3"));
            outputLine.put("segment_end", fields.get("field4"));
            StringBuilder transcription = new StringBuilder();
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

            Segment segment = new Segment();
            segment.setFile_name(fileName);
            segment.setSpeaker(outputLine.get("speaker"));
            segment.setSegment_start(Double.parseDouble(outputLine.get("segment_start")));
            segment.setSegment_end(Double.parseDouble(outputLine.get("segment_end")));
            segment.setTranscription(outputLine.get("transcription"));
            segment.setFile(fileEntity);
            segmentRepository.save(segment);
            fileEntity.getSegments().add(segment);
        }

        fileRepository.save(fileEntity);

        return ResponseEntity.ok(outputLines);
    }

}

