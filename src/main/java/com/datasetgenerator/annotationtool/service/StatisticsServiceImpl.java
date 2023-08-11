package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.model.Segment;
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
    public List<Map<String, Object>> getDatasetStatistics() {
        List<Object[]> result = fileRepository.getDatasetStatistics();
        List<Map<String, Object>> filesStatistics = new ArrayList<>();
        for (Object[] row : result) {
            Double totalDuration = (Double) row[0];
            Double averageDuration = (Double) row[1];
            Long segmentCount = (Long) row[2];
            Long speakerCount = (Long) row[3];

            Map<String, Object> fileDetails = new LinkedHashMap<>();
            fileDetails.put("totalDuration", totalDuration);
            fileDetails.put("averageDuration", averageDuration);
            fileDetails.put("segmentCount", segmentCount);
            fileDetails.put("speakerCount", speakerCount);
            filesStatistics.add(fileDetails);
        }
        return filesStatistics;
    }

    public List<Map<String, Object>> getFilesStatistics() {
        List<Object[]> result = segmentRepository.getFilesStatistics();
        List<Map<String, Object>> filesStatistics = new ArrayList<>();
        for (Object[] row : result) {
            Long fileId=(Long) row[0];
            String fileName = (String) row[1];
            Double totalDuration = (Double) row[2];
            Double averageDuration = (Double) row[3];
            Long segmentCount = (Long) row[4];
            Long speakerCount = (Long) row[5];
            LocalDateTime uploadTime = (LocalDateTime) row[6];
System.out.println(uploadTime);
            Map<String, Object> fileDetails = new LinkedHashMap<>();
            fileDetails.put("fileId", fileId);
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
    public List<Map<String, Object>> getFilesStatistics(List<Long> fileIds) {
        List<Segment> segments = segmentRepository.findAll();
        for (Long fileId : fileIds) {
            boolean fileIdExists = segments.stream().anyMatch(segment -> segment.getFile().getFile_id().equals(fileId));
            if (!fileIdExists) {
                throw new IllegalArgumentException("fileId " + fileId + " doesn't exist!");
            }
        }
        List<Object[]> result = segmentRepository.getFilesStatistics(fileIds);
        List<Map<String, Object>> filesStatistics = new ArrayList<>();
        for (Object[] row : result) {
            Double totalDuration = (Double) row[0];
            Double averageDuration = (Double) row[1];
            Long segmentCount = (Long) row[2];
            Long speakerCount = (Long) row[3];
            Map<String, Object> fileDetails = new LinkedHashMap<>();
            fileDetails.put("totalDuration", totalDuration);
            fileDetails.put("averageDuration", averageDuration);
            fileDetails.put("segmentCount", segmentCount);
            fileDetails.put("speakerCount", speakerCount);
            fileDetails.put("fileIds", fileIds);
            filesStatistics.add(fileDetails);
        }
        return filesStatistics;
    }
}