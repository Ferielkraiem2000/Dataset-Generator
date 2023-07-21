package com.datasetgenerator.annotationtool.repository;

import com.datasetgenerator.annotationtool.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SegmentRepository extends JpaRepository<Segment,Long> {
   @Query("SELECT COUNT(s.segment_id), AVG(s.segment_end - s.segment_start), SUM(s.segment_end -s.segment_start) FROM Segment s JOIN s.file f where f.file_id= :file_id")
    List<Object[]> getDetailsForEachFile(@Param("file_id") Long file_id);

}
