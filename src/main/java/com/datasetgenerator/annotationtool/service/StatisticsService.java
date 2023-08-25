package com.datasetgenerator.annotationtool.service;

import java.util.List;
import java.util.Map;

import com.datasetgenerator.annotationtool.util.HistogramData;

public interface StatisticsService {
    public List<Map<String, Object>> getDatasetStatistics();

    List<Map<String, Object>> getFilesStatistics();

    public List<Map<String, Object>> getFilesStatisticsByFileName(String Name);

    List<Map<String, Object>> getFilesStatistics(List<Long> fileIds);

    HistogramData getHistogramData( double intervalSize);

    HistogramData getHistogramDataForSelectedFiles(List<Long> fileIds,double intervalSize);
}
