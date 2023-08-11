package com.datasetgenerator.annotationtool.service;


import com.datasetgenerator.annotationtool.model.File;
import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.FileRepository;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FileParseServiceImpl implements FileParseService {

    private final ExtractFileContentsService fileExtractContentService;
    private final FileRepository fileRepository;
    private final SegmentRepository segmentRepository;
    @Value("${file.extensions}")
    private String fileExtensions;
    private int minimumFields;

    public FileParseServiceImpl(ExtractFileContentsService fileExtractContentService, FileRepository fileRepository, SegmentRepository segmentRepository, @Value("${minimumFields}") int minimumFields) {
        this.fileExtractContentService = fileExtractContentService;
        this.fileRepository = fileRepository;
        this.segmentRepository = segmentRepository;
        this.minimumFields = minimumFields;
    }

    public List<String> extractValidLines(MultipartFile file) throws IOException {
        String fileContent = fileExtractContentService.readFileContent(file);
        List<String> validLines = new ArrayList<>();
        String[] lines = fileContent.split("\n");
        for (String line : lines) {
            String[] fields = line.split(" ");
            if (fields.length >= minimumFields && !line.startsWith(";;")) {
                validLines.add(line);
            }
        }
        return validLines;
    }


    public List<List<String>> extractLineContent(MultipartFile file) throws IOException {

        List<String> validLines = extractValidLines(file);
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
        return lineContent;
    }

    public String extractFileName(List<String> fields) throws IOException {
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

        return fileName;
    }

    public StringBuilder extractTranscription(List<String> fields) {
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
        return transcription;
    }


    public List<Map<String, String>> extractFields(MultipartFile file, boolean overwrite) throws IOException {
        List<String> validLines = extractValidLines(file);
        List<List<String>> lineContentResponse = extractLineContent(file);
        List<List<String>> lineContent = lineContentResponse;
        List<Map<String, String>> outputLines = new ArrayList<>();
        String fileName = "";
        File fileEntity = new File();
        fileRepository.save(fileEntity);
        for (int i = 0; i < Objects.requireNonNull(validLines).size(); i++) {
            assert lineContent != null;
            List<String> fields = lineContent.get(i);
            Map<String, String> outputLine = new LinkedHashMap<>();
            fileName = extractFileName(fields);
            outputLine.put("file_name", fileName);
            outputLine.put("speaker", fields.get(1));
            outputLine.put("segment_start", fields.get(2));
            outputLine.put("segment_end", fields.get(3));
            double segmentStart = Double.parseDouble(outputLine.get("segment_start"));
            double segmentEnd = Double.parseDouble(outputLine.get("segment_end"));
            double duration = segmentEnd - segmentStart;
            StringBuilder transcription = extractTranscription(fields);
            outputLine.put("transcription", String.valueOf(transcription));
            outputLines.add(outputLine);
            File existingFile = fileRepository.existsByFileName(fileName);
            if (existingFile != null) {
                if (overwrite) {
                    fileRepository.deleteSegmentsByFileId(existingFile.getFile_id());
                    fileRepository.deleteByFileName(fileName);
                    Segment segment = new Segment();
                    segment.setSpeaker(outputLine.get("speaker"));
                    segment.setSegment_start(Double.parseDouble(outputLine.get("segment_start")));
                    segment.setSegment_end(Double.parseDouble(outputLine.get("segment_end")));
                    segment.setDuration(duration);
                    segment.setTranscription(outputLine.get("transcription"));
                    segment.setFile(fileEntity);
                    segment.setDuration(duration);
                    segmentRepository.save(segment);
                } else {
                    throw new RuntimeException("File already exists!");
                }
            } else {
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
        }

        if (overwrite || fileRepository.existsByFileName(fileName) == null) {
            fileEntity.setFile_name(fileName);
            fileEntity.setUpload_time(LocalDateTime.now());
            fileRepository.save(fileEntity);
        }
        else{
            throw new RuntimeException("File already exists!");
        }
        return outputLines;
    }
    public List<Map<String, String>> showContent(Long fileId){
        List<Segment> segments=segmentRepository.findAllById(fileId);
        List<Map<String, String>> outputLines = new ArrayList<>();
        for(Segment segment : segments){
            Map<String, String> outputLine = new LinkedHashMap<>();
            outputLine.put("file name", segment.getFile().getFile_name());
            outputLine.put("speaker", segment.getSpeaker());
            outputLine.put("segment_start", String.valueOf(segment.getSegment_start()));
            outputLine.put("segment_end", String.valueOf(segment.getSegment_end()));
            outputLine.put("transcription", segment.getTranscription());
            outputLines.add(outputLine);

        }
        return outputLines;
    }
}


