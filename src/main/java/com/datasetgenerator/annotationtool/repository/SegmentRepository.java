package com.datasetgenerator.annotationtool.repository;

import com.datasetgenerator.annotationtool.model.File;
import com.datasetgenerator.annotationtool.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentRepository extends JpaRepository<Segment,Long> {
    @Query("SELECT SUM(s.duration), AVG (s.duration),COUNT(s.segment_id) FROM Segment s  ")
    List< Object[]> getStatistics();
    @Query("SELECT SUM(s.duration), AVG(s.duration), COUNT(s.segment_id) FROM Segment s JOIN s.file f WHERE f.file_id IN :fileIds ")
    List<Object[]> getStatisticsForEachFile(@Param("fileIds") List<Long> fileIds);
    @Query("select s FROM Segment s  WHERE s.file = :file")
    List<Segment> findByFile(@Param("file") File file);



}
