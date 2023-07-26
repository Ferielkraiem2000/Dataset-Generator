package com.datasetgenerator.annotationtool.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface DownloadManifestFileService  {
     ResponseEntity<String> downloadCombinedManifestInJsonFormat();
     ResponseEntity<String> downloadCombinedManifestInCsvFormat() throws IOException;
     ResponseEntity<ByteArrayResource> downloadTextFileForTranscriptions() throws IOException;
     public ResponseEntity<ByteArrayResource> downloadUtt2SpkFile() throws IOException;
     public ResponseEntity<ByteArrayResource> downloadFileForSegmentsDescription() throws IOException ;
     public ResponseEntity<ByteArrayResource> downloadFileCompatibleWithESPnet() throws IOException ;

     }
