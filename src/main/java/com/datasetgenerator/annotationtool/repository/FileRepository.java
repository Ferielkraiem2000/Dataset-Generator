package com.datasetgenerator.annotationtool.repository;

import com.datasetgenerator.annotationtool.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("select count(f)>0 from File f where f.file_name= :fileName")
    boolean existsByFileName(@Param("fileName") String fileName);

    @Query("select count(f)>0 from File f where f.file_id= :fileId")
    boolean findByFileId(@Param("fileId") Long fileId);

    @Transactional
    @Modifying
    @Query("update File f set f.file_name= :fileName where f.file_id= :fileId")
    void updateFileName(@Param("fileId") Long fileId, @Param("fileName") String fileName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Segment s WHERE s.file.file_id= :fileId")
    void deleteSegmentsByFileId(@Param("fileId") Long fileId);

}
