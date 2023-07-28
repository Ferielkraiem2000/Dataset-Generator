package com.datasetgenerator.annotationtool.service;

import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.List;

public interface DownloadManifestFileService {
    String createCombinedManifestInJsonFormat(String path, List<String> fileIds);

    String createCombinedManifestInCsvFormat(String path, List<String> fileIds) throws IOException;

    ByteArrayResource createFileCompatibleWithESPnet(String path, List<String> fileIds) throws IOException;

}
