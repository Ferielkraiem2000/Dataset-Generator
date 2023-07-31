package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.repository.FileRepository;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUploadedFileServiceImpl implements UpdateUploadedFileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileParseService fileParseService;

    @Autowired
    private SegmentRepository segmentRepository;

    public void updateFileName(Long file_id, String fileName) {
        if (fileRepository.findByFileId(file_id)) {
            fileRepository.updateFileName(file_id, fileName);
        }
        throw new IllegalArgumentException("fileId doesn't exist!");
    }
}
