package com.datasetgenerator.annotationtool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "segment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Segment {

    @Id
    @SequenceGenerator(name = "segment_sequence", sequenceName = "segment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "segment_sequence")
    @Column(name = "segment_id")
    private Long segment_id;
    @Column(name = "speaker")
    private String speaker;
    @NotNull
    @Column(name = "segment_start")
    private double segment_start;
    @NotNull
    @Column(name = "segment_end")
    private double segment_end;
    @NotBlank
    @Column(name = "transcription")
    private String transcription;
    @Column(name = "duration")
    private double duration;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;
}
