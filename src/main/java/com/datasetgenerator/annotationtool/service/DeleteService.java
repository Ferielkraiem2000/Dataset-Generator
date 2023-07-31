package com.datasetgenerator.annotationtool.service;

import java.util.List;

public interface DeleteService {
    void deleteSegmentsByFileIds(List<Long> fileIds);
}
