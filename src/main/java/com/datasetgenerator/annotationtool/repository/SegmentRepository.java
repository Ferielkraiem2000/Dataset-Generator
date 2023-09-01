package com.datasetgenerator.annotationtool.repository;

import com.datasetgenerator.annotationtool.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {

    @Query("SELECT   f.file_id, f.file_name, SUM(s.duration), AVG(s.duration), COUNT(s.segment_id), COUNT(distinct s.speaker), s.file.upload_time FROM Segment s JOIN s.file f  group by f.file_id")
    List<Object[]> getFilesStatistics();
    @Query("SELECT   f.file_id, f.file_name, SUM(s.duration), AVG(s.duration), COUNT(s.segment_id), COUNT(distinct s.speaker), s.file.upload_time FROM Segment s JOIN s.file f  where f.file_name= :fileName group by f.file_id")
    List<Object[]> getFilesStatisticsByFileName(@Param("fileName") String fileName);
    @Query("SELECT  SUM(s.duration), AVG(s.duration), COUNT(s.segment_id), COUNT(DISTINCT s.speaker) FROM Segment s JOIN s.file f WHERE f.file_id IN :fileIds")
    List<Object[]> getFilesStatistics(@Param("fileIds") List<Long> fileIds);


    @Query("SELECT s , f.file_id from Segment s JOIN  s.file f where f.file_id= :fileId")
    List<Segment> findAllById(@Param("fileId") Long fileId);

    @Query("SELECT s.duration FROM Segment s ")
    List<Double> getDurationsSegments();
    @Query("SELECT s.duration  FROM Segment s JOIN s.file f WHERE f.file_id IN :fileIds ")
    List<Double> getDurationsSegmentsForFiles(@Param("fileIds") List<Long> fileIds);
}
