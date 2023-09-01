package com.datasetgenerator.annotationtool.repository;
import com.datasetgenerator.annotationtool.model.IntervalData;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalDataRepository extends JpaRepository<IntervalData,Long> {
    IntervalData findByStartIntervalAndEndInterval(double startInterval, double endInterval);

}
