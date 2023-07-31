package com.datasetgenerator.annotationtool.util;

import java.util.List;

public class HistogramData {

    private List<Interval> intervals;
    private List<Long> segmentCountPerInterval;

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public List<Long> getSegmentCountPerInterval() {
        return segmentCountPerInterval;
    }

    public void setSegmentCountPerInterval(List<Long> segmentCountPerInterval) {
        this.segmentCountPerInterval = segmentCountPerInterval;
    }
}
