package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.repository.FileRepository;
import com.datasetgenerator.annotationtool.repository.IntervalDataRepository;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    SegmentRepository segmentRepository;
    FileRepository fileRepository;
    IntervalDataRepository intervalDataRepository;

    StatisticsServiceImpl(SegmentRepository segmentRepository, IntervalDataRepository intervalDataRepository, FileRepository fileRepository) {
        this.segmentRepository = segmentRepository;
        this.intervalDataRepository = intervalDataRepository;
        this.fileRepository = fileRepository;
    }

    public List<Map<String, Object>> getStatistics() {
        List<Object[]> result = segmentRepository.getStatistics();
        List<Map<String, Object>> filesStatistics = new ArrayList<>();
        for (Object[] row : result) {
            String fileName = (String) row[0];
            Double totalDuration = (Double) row[1];
            Double averageDuration = (Double) row[2];
            Long segmentCount = (Long) row[3];
            Long speakerCount = (Long) row[4];
            LocalDateTime uploadTime = (LocalDateTime) row[5];
            Map<String, Object> fileDetails = new LinkedHashMap<>();
            fileDetails.put("fileName", fileName);
            fileDetails.put("totalDuration", totalDuration);
            fileDetails.put("averageDuration", averageDuration);
            fileDetails.put("segmentCount", segmentCount);
            fileDetails.put("speakerCount", speakerCount);
            fileDetails.put("uploadTime", uploadTime);
            filesStatistics.add(fileDetails);
        }
        return filesStatistics;
    }
}