package com.datasetgenerator.annotationtool.repository;


import com.datasetgenerator.annotationtool.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,String> {
}
