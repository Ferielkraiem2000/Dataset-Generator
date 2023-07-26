package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.util.HistogramData;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface StatisticsService {
    ResponseEntity<Map<String, Object>> getStatistics();
    ResponseEntity<HistogramData> getHistogramData();

    }
