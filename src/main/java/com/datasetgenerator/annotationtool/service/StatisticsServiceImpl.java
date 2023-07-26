package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.model.IntervalData;
import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.IntervalDataRepository;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import com.datasetgenerator.annotationtool.util.HistogramData;
import com.datasetgenerator.annotationtool.util.Interval;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    SegmentRepository segmentRepository;
    IntervalDataRepository intervalDataRepository;

    StatisticsServiceImpl(SegmentRepository segmentRepository, IntervalDataRepository intervalDataRepository) {
        this.segmentRepository = segmentRepository;
        this.intervalDataRepository = intervalDataRepository;
    }

    public ResponseEntity<Map<String, Object>> getStatistics() {
        List<Object[]> result = segmentRepository.getStatistics();
        Map<String, Object> fileDetails = new LinkedHashMap<>();
        for (Object[] row : result) {

            Double totalDuration = (Double) row[0];
            Double averageDuration = (Double) row[1];
            Long segmentCount = (Long) row[2];
            Long speakerCount = (Long) row[3];
            fileDetails.put("totalDuration", totalDuration);
            fileDetails.put("averageDuration", averageDuration);
            fileDetails.put("segmentCount", segmentCount);
            fileDetails.put("speakerCount", speakerCount);
        }
        return ResponseEntity.ok(fileDetails);
    }

    public ResponseEntity<HistogramData> getHistogramData() {
        List<Segment> segments = segmentRepository.findAll();
        List<Interval> durationIntervals = new ArrayList<>();
        List<Long> segmentCountPerInterval = new ArrayList<>();
        double intervalSize = 0.1;
        double startInterval = 0.0;
        double endInterval = intervalSize;
        int intervalSegmentCount = 0;
        for (Segment segment : segments) {
            double segmentDuration = (double) segment.getDuration();
            while (segmentDuration >= endInterval) {
                startInterval = Math.round(startInterval * 10.0) / 10.0;
                endInterval = Math.round(endInterval * 10.0) / 10.0;
                durationIntervals.add(new Interval(startInterval, endInterval));
                segmentCountPerInterval.add((long) intervalSegmentCount);
                IntervalData existingIntervalData = intervalDataRepository.findByStartIntervalAndEndInterval(startInterval, endInterval);
                if (existingIntervalData == null) {
                    IntervalData intervalData = new IntervalData();
                    intervalData.setStartInterval(startInterval);
                    intervalData.setEndInterval(endInterval);
                    intervalData.setSegmentCount((long) intervalSegmentCount);
                    intervalDataRepository.save(intervalData);
                } else {
                    existingIntervalData.setSegmentCount(existingIntervalData.getSegmentCount() + intervalSegmentCount);
                    intervalDataRepository.save(existingIntervalData);
                }
                startInterval = endInterval;
                endInterval += intervalSize;
                intervalSegmentCount=0;
            }
            if (segmentDuration >= startInterval && segmentDuration<endInterval) {
                intervalSegmentCount++;
                startInterval = Math.round(startInterval * 10.0) / 10.0;
                endInterval = Math.round(endInterval * 10.0) / 10.0;
                durationIntervals.add(new Interval(startInterval, endInterval));
                segmentCountPerInterval.add((long) intervalSegmentCount);
                IntervalData existingIntervalData = intervalDataRepository.findByStartIntervalAndEndInterval(startInterval, endInterval);
                if (existingIntervalData == null) {
                    IntervalData intervalData = new IntervalData();
                    intervalData.setStartInterval(startInterval);
                    intervalData.setEndInterval(endInterval);
                    intervalData.setSegmentCount((long) intervalSegmentCount);
                    intervalDataRepository.save(intervalData);
                } else {
                    existingIntervalData.setSegmentCount(existingIntervalData.getSegmentCount() + intervalSegmentCount);
                    intervalDataRepository.save(existingIntervalData);
                }
            }
        }
        HistogramData histogramData = new HistogramData();
        histogramData.setIntervals(durationIntervals);
        histogramData.setSegmentCountPerInterval(segmentCountPerInterval);
        return ResponseEntity.ok(histogramData);
    }
}