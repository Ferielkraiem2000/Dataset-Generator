package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.model.File;
import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.FileRepository;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class UpdateUploadedFileServiceImpl implements UpdateUploadedFileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileParseService fileParseService;

    @Autowired
    private SegmentRepository segmentRepository;

    public void updateFile(MultipartFile file) throws IOException {
        List<Map<String, String>> fields = fileParseService.extractFields(file).getBody();
        String fileName = "";
        boolean existsFile=false;
        for (int i = 0; i < fields.size(); i++) {
            Map<String, String> fieldMap = fields.get(i);
            System.out.println(fieldMap);
            fileName = fieldMap.get("file_name");
            File existingFile = fileRepository.findByFileName(fileName);
            if (existingFile != null) {
                existsFile=true;
                fileRepository.delete(existingFile);
                List<Segment> existingSegments = segmentRepository.findByFile(existingFile);
                segmentRepository.deleteAll(existingSegments);
                Segment segment = new Segment();
                segment.setSpeaker(fieldMap.get("speaker"));
                segment.setSegment_start(Double.parseDouble(fieldMap.get("segment_start")));
                segment.setSegment_end(Double.parseDouble(fieldMap.get("segment_end")));
                segment.setDuration(Double.parseDouble(fieldMap.get("duration")));
                segment.setTranscription(fieldMap.get("transcription"));
                segment.setFile(existingFile);
                segmentRepository.save(segment);
            }
        }
    }
}
