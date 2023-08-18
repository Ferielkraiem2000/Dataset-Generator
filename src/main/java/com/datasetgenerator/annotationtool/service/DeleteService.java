package com.datasetgenerator.annotationtool.service;

import java.util.List;

public interface DeleteService {
    void deleteByFileIds(List<Long> fileIds);
}
