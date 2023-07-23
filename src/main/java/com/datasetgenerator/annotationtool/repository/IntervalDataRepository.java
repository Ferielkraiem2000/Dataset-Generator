package com.datasetgenerator.annotationtool.repository;

import com.datasetgenerator.annotationtool.model.IntervalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervalDataRepository extends JpaRepository<IntervalData,Long> {
    IntervalData findByStartIntervalAndEndInterval(double startInterval, double endInterval);
}
