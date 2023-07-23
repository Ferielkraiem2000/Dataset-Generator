package com.datasetgenerator.annotationtool.repository;

import com.datasetgenerator.annotationtool.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentRepository extends JpaRepository<Segment,Long> {

}
