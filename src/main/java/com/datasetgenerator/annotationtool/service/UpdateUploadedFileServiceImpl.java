package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUploadedFileServiceImpl implements UpdateUploadedFileService {

    @Autowired
    private FileRepository fileRepository;

    public void updateFileName(Long file_id, String fileName) {
        if (fileRepository.findByFileId(file_id) && !fileName.equals("")) {
            fileRepository.updateFileName(file_id, fileName);
        }
    }
}