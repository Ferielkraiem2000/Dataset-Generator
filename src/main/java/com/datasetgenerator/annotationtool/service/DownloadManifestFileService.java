package com.datasetgenerator.annotationtool.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface DownloadManifestFileService  {
     ResponseEntity<String> downloadCombinedManifestInJsonFormat(String path, List<String> fileIds);
     ResponseEntity<String> downloadCombinedManifestInCsvFormat(String path, List<String> fileIds) throws IOException;
     ResponseEntity<ByteArrayResource> downloadTextFileForTranscriptions(List<String> fileIds) throws IOException;
     public ResponseEntity<ByteArrayResource> downloadPathsFile( String path,List<String> fileIds) throws IOException;
     public ResponseEntity<ByteArrayResource> downloadUtt2SpkFile( List<String> fileIds) throws IOException;
     public ResponseEntity<ByteArrayResource> downloadFileForSegmentsDescription(String path, List<String> fileIds) throws IOException ;
     public ResponseEntity<ByteArrayResource> downloadFileCompatibleWithESPnet(String path, List<String> fileIds) throws IOException ;

     }
