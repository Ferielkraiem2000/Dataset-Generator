package com.datasetgenerator.annotationtool.util;

public class Interval {
    Double start;
    Double end;

    public Interval(Double start, Double end) {
        this.start = start;
        this.end = end;
    }

    public double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
    }
}
