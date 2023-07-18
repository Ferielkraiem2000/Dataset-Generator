package com.datasetgenerator.annotationtool.repository;


import com.datasetgenerator.annotationtool.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends JpaRepository<File,Long> {
}
