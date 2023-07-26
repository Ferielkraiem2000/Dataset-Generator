package com.datasetgenerator.annotationtool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interval_data")
public class IntervalData {

    @Id
    @SequenceGenerator(name = "interval_sequence", sequenceName = "interval_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interval_sequence")
    @Column(name = "interval_id")
    private Long intervalId;

    @Column(name = "start_interval")
    private Double startInterval;

    @Column(name = "end_interval")
    private Double endInterval;

    @Column(name = "segment_count")
    private Long segmentCount;

}
