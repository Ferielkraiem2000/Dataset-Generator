package com.datasetgenerator.annotationtool.model;

import com.datasetgenerator.annotationtool.util.Interval;

import java.util.List;

public class HistogramData {

    private List<Interval> intervals;
    private List<Long> fileCountPerInterval;

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public List<Long> getFileCountPerInterval() {
        return fileCountPerInterval;
    }

    public void setFileCountPerInterval(List<Long> fileCountPerInterval) {
        this.fileCountPerInterval = fileCountPerInterval;
    }
}
