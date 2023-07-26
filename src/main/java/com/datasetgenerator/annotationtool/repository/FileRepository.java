package com.datasetgenerator.annotationtool.repository;
import com.datasetgenerator.annotationtool.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends JpaRepository<File,Long> {
    @Query("select count(f)>0 from File f where f.file_name= :fileName")
    boolean existsByFileName(String fileName);
}
