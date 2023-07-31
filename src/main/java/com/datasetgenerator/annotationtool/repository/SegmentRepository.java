package com.datasetgenerator.annotationtool.repository;

import com.datasetgenerator.annotationtool.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {
    @Query("SELECT f.file_name, SUM(s.duration), AVG (s.duration), COUNT(s.segment_id), COUNT(distinct s.speaker), s.file.upload_time FROM Segment s JOIN s.file f  group by f.file_id")
    List<Object[]> getStatistics();
}
