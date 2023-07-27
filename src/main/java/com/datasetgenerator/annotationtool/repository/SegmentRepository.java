package com.datasetgenerator.annotationtool.repository;

import com.datasetgenerator.annotationtool.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SegmentRepository extends JpaRepository<Segment,Long> {
    @Query("SELECT SUM(s.duration), AVG (s.duration),COUNT(s.segment_id) FROM Segment s JOIN s.file f ")
    List< Object[]> getStatistics();
    @Query("SELECT SUM(s.duration), AVG(s.duration), COUNT(s.segment_id) FROM Segment s JOIN s.file f WHERE f.file_id IN :fileIds ")
    List<Object[]> getStatisticsForEachFile(@Param("fileIds") List<Long> fileIds);


}
