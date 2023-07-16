package com.datasetgenerator.annotationtool.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "file")
public class File implements Serializable {

    @Id
    @Column(name="file_name")
    private String fileName;

    @Column(name="speaker_id")
    private String speakerId;
    @Column(name="segment_start")
    private String segmentStart;
    @Column(name="segment_end")
    private String segmentEnd;

    @Column(name="transcription")
    private String transcription;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public String getSegmentStart() {
        return segmentStart;
    }

    public void setSegmentStart(String segmentStart) {
        this.segmentStart = segmentStart;
    }

    public String getSegmentEnd() {
        return segmentEnd;
    }

    public void setSegmentEnd(String segmentEnd) {
        this.segmentEnd = segmentEnd;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public File() {
    }
}
