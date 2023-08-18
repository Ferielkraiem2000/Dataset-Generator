package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteServiceImpl implements DeleteService {
    @Autowired
    FileRepository fileRepository;

    public void deleteByFileIds(List<Long> fileIds) {
        for (Long fileId : fileIds) {
            if (fileRepository.findByFileId(fileId)) {
                fileRepository.deleteSegmentsByFileId(fileId);
                fileRepository.deleteById(fileId);
            }
        }
    }
}
