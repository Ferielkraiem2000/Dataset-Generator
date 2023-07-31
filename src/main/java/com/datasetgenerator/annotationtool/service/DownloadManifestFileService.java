package com.datasetgenerator.annotationtool.service;

import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.List;

public interface DownloadManifestFileService {
    String createCombinedManifestInJsonFormat(String path, List<Long> fileIds);

    String createCombinedManifestInCsvFormat(String path, List<Long> fileIds) throws IOException;

    ByteArrayResource createFileCompatibleWithESPnet(String path, List<Long> fileIds) throws IOException;
    ByteArrayResource createCombinedManifest(String format, String path, List<Long> fileIds) throws IOException ;
}
