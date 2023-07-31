package com.datasetgenerator.annotationtool.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file")
public class File {

    @Id
    @SequenceGenerator(name = "file_sequence", sequenceName = "file_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_sequence")
    @Column(name = "file_id")
    private Long file_id;
    @NotBlank
    @Column(name = "file_name")
    private String file_name;
    @Column(name="upload_time")
    private LocalDateTime upload_time;
    @OneToMany(mappedBy = "file")
    private List<Segment> segments = new ArrayList<>();
}


