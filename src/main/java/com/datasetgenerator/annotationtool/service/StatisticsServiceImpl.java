package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.model.IntervalData;
import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.FileRepository;
import com.datasetgenerator.annotationtool.repository.IntervalDataRepository;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import com.datasetgenerator.annotationtool.util.HistogramData;
import com.datasetgenerator.annotationtool.util.Interval;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    SegmentRepository segmentRepository;
    FileRepository fileRepository;
    IntervalDataRepository intervalDataRepository;
    @Autowired
    private ObjectMapper objectMapper;

    StatisticsServiceImpl(SegmentRepository segmentRepository, IntervalDataRepository intervalDataRepository,
            FileRepository fileRepository) {
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
            Long fileId = (Long) row[0];
            String fileName = (String) row[1];
            Double totalDuration = (Double) row[2];
            Double averageDuration = (Double) row[3];
            Long segmentCount = (Long) row[4];
            Long speakerCount = (Long) row[5];
            LocalDateTime uploadTime = (LocalDateTime) row[6];
            Map<String, Object> fileDetails = new LinkedHashMap<>();
            fileDetails.put("fileId", fileId);
            fileDetails.put("fileName", fileName);
            fileDetails.put("totalDuration", totalDuration);
            fileDetails.put("averageDuration", averageDuration);
            fileDetails.put("segmentCount", segmentCount);
            fileDetails.put("speakerCount", speakerCount);
            fileDetails.put("uploadTime", objectMapper.convertValue(uploadTime, String.class));
            filesStatistics.add(fileDetails);
        }
        return filesStatistics;
    }

    public List<Map<String, Object>> getFilesStatisticsByFileName(String Name) {
        List<Object[]> result = segmentRepository.getFilesStatisticsByFileName(Name);
        List<Map<String, Object>> filesStatistics = new ArrayList<>();
        for (Object[] row : result) {
            Long fileId = (Long) row[0];
            String fileName = (String) row[1];
            Double totalDuration = (Double) row[2];
            Double averageDuration = (Double) row[3];
            Long segmentCount = (Long) row[4];
            Long speakerCount = (Long) row[5];
            LocalDateTime uploadTime = (LocalDateTime) row[6];
            Map<String, Object> fileDetails = new LinkedHashMap<>();
            fileDetails.put("fileId", fileId);
            fileDetails.put("fileName", fileName);
            fileDetails.put("totalDuration", totalDuration);
            fileDetails.put("averageDuration", averageDuration);
            fileDetails.put("segmentCount", segmentCount);
            fileDetails.put("speakerCount", speakerCount);
            fileDetails.put("uploadTime", objectMapper.convertValue(uploadTime, String.class));
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

    public HistogramData getHistogramData(double intervalSize) {
        List<Double> result = segmentRepository.getDurationsSegments();
        Collections.sort(result);
        List<IntervalData> intervalDataList = new ArrayList<>();
        double currentIntervalStart = 0.0;
        double currentIntervalEnd = intervalSize;
        int intervalSegmentCount = 0;
        for (Double duration : result) {
            double segmentDuration = roundToTwoDecimal(duration);
            List<IntervalData> segmentIntervalDataList = new ArrayList<>();

            while (segmentDuration > currentIntervalEnd) {
                IntervalData intervalData = new IntervalData();
                intervalData.setStartInterval(roundToTwoDecimal(currentIntervalStart));
                intervalData.setEndInterval(roundToTwoDecimal(currentIntervalEnd));
                intervalData.setSegmentCount((long) intervalSegmentCount);
                segmentIntervalDataList.add(intervalData);
                currentIntervalStart = currentIntervalEnd;
                currentIntervalEnd = roundToTwoDecimal(currentIntervalEnd + intervalSize);
                intervalSegmentCount = 0;
            }
            if (segmentDuration >= currentIntervalStart) {
                intervalSegmentCount++;
            }
            intervalDataList.addAll(segmentIntervalDataList);

        }
        List<IntervalData> segmentIntervalDataList = new ArrayList<>();

        if (intervalSegmentCount >= 0) {
            IntervalData intervalData = new IntervalData();
            intervalData.setStartInterval(roundToTwoDecimal(currentIntervalStart));
            intervalData.setEndInterval(roundToTwoDecimal(currentIntervalEnd));
            intervalData.setSegmentCount((long) intervalSegmentCount);
            intervalDataList.add(intervalData);
            segmentIntervalDataList.add(intervalData);

        }
        intervalDataList.addAll(segmentIntervalDataList);

        for (IntervalData intervalData : intervalDataList) {
            IntervalData existingIntervalData = intervalDataRepository.findByStartIntervalAndEndInterval(
                    intervalData.getStartInterval(), intervalData.getEndInterval());

            if (existingIntervalData == null) {
                intervalDataRepository.save(intervalData);
            } else {
                existingIntervalData
                        .setSegmentCount(existingIntervalData.getSegmentCount() + intervalData.getSegmentCount());
                intervalDataRepository.save(existingIntervalData);
            }
        }

        List<Interval> durationIntervals = new ArrayList<>();
        List<Long> segmentCountPerInterval = new ArrayList<>();

        for (IntervalData intervalData : intervalDataList) {
            durationIntervals.add(new Interval(intervalData.getStartInterval(), intervalData.getEndInterval()));
            segmentCountPerInterval.add(intervalData.getSegmentCount());
        }

        HistogramData histogramData = new HistogramData();
        histogramData.setIntervals(durationIntervals);
        histogramData.setSegmentCountPerInterval(segmentCountPerInterval);
        return histogramData;
    }

    public HistogramData getHistogramDataForSelectedFiles(List<Long> fileIds, double intervalSize) {
        List<Double> result = segmentRepository.getDurationsSegmentsForFiles(fileIds);
        Collections.sort(result);
        double currentIntervalStart = 0.0;
        double currentIntervalEnd = intervalSize;
        int intervalSegmentCount = 0;
        List<IntervalData> intervalDataList = new ArrayList<>();

       for (Double duration : result){
            double segmentDuration = roundToTwoDecimal((double) duration);

            List<IntervalData> segmentIntervalDataList = new ArrayList<>();

            while (segmentDuration > currentIntervalEnd) {
                IntervalData intervalData = new IntervalData();
                intervalData.setStartInterval(roundToTwoDecimal(currentIntervalStart));
                intervalData.setEndInterval(roundToTwoDecimal(currentIntervalEnd));
                intervalData.setSegmentCount((long) intervalSegmentCount);
                segmentIntervalDataList.add(intervalData);
                currentIntervalStart = currentIntervalEnd;
                currentIntervalEnd = roundToTwoDecimal(currentIntervalEnd + intervalSize);
                intervalSegmentCount = 0;

            }

            if (segmentDuration >= currentIntervalStart) {
                intervalSegmentCount++;
            }

            intervalDataList.addAll(segmentIntervalDataList);

        }
        List<IntervalData> segmentIntervalDataList = new ArrayList<>();
        if (intervalSegmentCount >= 0) {
            IntervalData intervalData = new IntervalData();
            intervalData.setStartInterval(roundToTwoDecimal(currentIntervalStart));
            intervalData.setEndInterval(roundToTwoDecimal(currentIntervalEnd));
            intervalData.setSegmentCount((long) intervalSegmentCount);
            segmentIntervalDataList.add(intervalData);
        }
        intervalDataList.addAll(segmentIntervalDataList);

        for (IntervalData intervalData : intervalDataList) {

            IntervalData existingIntervalData = intervalDataRepository.findByStartIntervalAndEndInterval(
                    intervalData.getStartInterval(), intervalData.getEndInterval());

            if (existingIntervalData == null) {
                intervalDataRepository.save(intervalData);
            } else {

                existingIntervalData
                        .setSegmentCount(existingIntervalData.getSegmentCount() + intervalData.getSegmentCount());
                intervalDataRepository.save(existingIntervalData);

            }
        }

        List<Interval> durationIntervals = new ArrayList<>();
        List<Long> segmentCountPerInterval = new ArrayList<>();
        for (IntervalData intervalData : intervalDataList) {
            durationIntervals.add(new Interval(intervalData.getStartInterval(), intervalData.getEndInterval()));
            segmentCountPerInterval.add(intervalData.getSegmentCount());
        }

        HistogramData histogramData = new HistogramData();
        histogramData.setIntervals(durationIntervals);
        histogramData.setSegmentCountPerInterval(segmentCountPerInterval);
        return histogramData;
    }

    private double roundToTwoDecimal(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}