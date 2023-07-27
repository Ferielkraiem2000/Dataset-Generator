package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service

public class DownloadManifestFileServiceImpl implements DownloadManifestFileService {

    SegmentRepository segmentRepository;

    public DownloadManifestFileServiceImpl(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    public ResponseEntity<String> downloadCombinedManifestInJsonFormat(String path, List<String> fileIds) {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();

        for (Segment segment : segments) {
            if (fileIds.contains(String.valueOf(segment.getFile().getFile_id()))) {
                filteredSegments.add(segment);
            }
        }
        Map<String, Map<String, String>> combinedManifest = new LinkedHashMap<>();
        for (Segment segment : filteredSegments) {
            String segmentId = String.valueOf(segment.getSegment_id());
            String fileName = segment.getFile().getFile_name();
            Map<String, String> combinedEntry = new LinkedHashMap<>();
            combinedEntry.put("path", path + "/" + fileName);
            combinedEntry.put("start", String.valueOf(segment.getSegment_start()));
            combinedEntry.put("stop", String.valueOf(segment.getSegment_end()));
            combinedEntry.put("duration", String.valueOf(segment.getDuration()));
            combinedEntry.put("speaker", segment.getSpeaker());
            String transcription = segment.getTranscription().replace("\r", "");
            combinedEntry.put("transcription", transcription);
            combinedManifest.put(segmentId, combinedEntry);
        }
        String combinedManifestJson = new Gson().toJson(combinedManifest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=combined_manifest.json");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(combinedManifestJson);
    }

    public ResponseEntity<String> downloadCombinedManifestInCsvFormat(String path, List<String> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (fileIds.contains(String.valueOf(segment.getFile().getFile_id()))) {
                filteredSegments.add(segment);
            }
        }
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(new String[]{"ID", "path", "start", "stop", "duration", "transcription", "speaker"});
        for (Segment segment : filteredSegments) {
            String segmentId = String.valueOf(segment.getSegment_id());
            String fileName = segment.getFile().getFile_name();
            String escapedTranscription = segment.getTranscription();
            String[] rowData = new String[]{
                    segmentId,
                    path + "/" + fileName,
                    String.valueOf(segment.getSegment_start()),
                    String.valueOf(segment.getSegment_end()),
                    String.valueOf(segment.getDuration()),
                    escapedTranscription,
                    segment.getSpeaker()
            };
            csvWriter.writeNext(rowData);
        }
        csvWriter.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=combined_manifest.csv");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(writer.toString());
    }

    public ResponseEntity<ByteArrayResource> downloadTextFileForTranscriptions(List<String> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (fileIds.contains(String.valueOf(segment.getFile().getFile_id()))) {
                filteredSegments.add(segment);
            }
        }
        Path filePath = Files.createTempFile("transcriptions", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Segment segment : filteredSegments) {
            String segment_id = String.valueOf(segment.getSegment_id());
            String transcription = segment.getTranscription();
            writer.write(segment_id + " " + transcription);
            writer.newLine();
        }
        writer.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transcriptions.txt");
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ByteArrayResource(content));
    }


    public ResponseEntity<ByteArrayResource> downloadUtt2SpkFile(List<String> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (fileIds.contains(String.valueOf(segment.getFile().getFile_id()))) {
                filteredSegments.add(segment);
            }
        }
        Path filePath = Files.createTempFile("utt2spk", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Segment segment : filteredSegments) {
            String segmentId = String.valueOf(segment.getSegment_id());
            String speaker = segment.getSpeaker();
            writer.write(segmentId + " " + speaker);
            writer.newLine();
        }
        writer.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=utt2spk.txt");
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ByteArrayResource(content));
    }

    public ResponseEntity<ByteArrayResource> downloadSpk2UttFile(List<String> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (fileIds.contains(String.valueOf(segment.getFile().getFile_id()))) {
                filteredSegments.add(segment);
            }
        }
        Map<String, List<String>> speakerToSegmentIdsMap = new HashMap<>();

        for (Segment segment : filteredSegments) {
            String speaker = segment.getSpeaker();
            speakerToSegmentIdsMap.putIfAbsent(speaker, new ArrayList<>());
            speakerToSegmentIdsMap.get(speaker).add(String.valueOf(segment.getSegment_id()));
        }
        Path filePath = Files.createTempFile("spk2utt", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Map.Entry<String, List<String>> entry : speakerToSegmentIdsMap.entrySet()) {
            String speaker = entry.getKey();
            List<String> segmentIds = entry.getValue();
            writer.write(speaker + " " + segmentIds);
            writer.newLine();
        }
        writer.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=spk2utt.txt");

        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ByteArrayResource(content));
    }


    public ResponseEntity<ByteArrayResource> downloadPathsFile(String path, List<String> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (fileIds.contains(String.valueOf(segment.getFile().getFile_id()))) {
                filteredSegments.add(segment);
            }
        }
        Path filePath = Files.createTempFile("wav", ".scp");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        List<String> segmentIds = new ArrayList<>();
        for (Segment segment : filteredSegments) {
            String segment_id = String.valueOf(segment.getSegment_id());
            String file_name = String.valueOf(segment.getFile().getFile_name());
            writer.write(segment_id + " " + path + "/" + file_name);
            writer.newLine();
        }
        writer.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=spk2utt.txt");

        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ByteArrayResource(content));
    }


    public ResponseEntity<ByteArrayResource> downloadFileForSegmentsDescription(String path, List<String> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (fileIds.contains(String.valueOf(segment.getFile().getFile_id()))) {
                filteredSegments.add(segment);
            }
        }
        Path filePath = Files.createTempFile("segments", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Segment segment : filteredSegments) {
            String utterance_id = String.valueOf(segment.getSegment_id());
            String file_name = segment.getFile().getFile_name();
            String wav_id = path + "/" + file_name;
            double start_time = segment.getSegment_start();
            double end_time = segment.getSegment_end();
            writer.write(utterance_id + " " + wav_id + " " + start_time + " " + end_time);
            writer.newLine();
        }

        writer.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=segments.txt");

        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ByteArrayResource(content));
    }

    public ResponseEntity<ByteArrayResource> downloadFileCompatibleWithESPnet(String path, List<String> fileIds) throws IOException {
        ByteArrayOutputStream zipOutput = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(zipOutput);

        ResponseEntity<ByteArrayResource> transcriptionsFile = downloadTextFileForTranscriptions(fileIds);
        ResponseEntity<ByteArrayResource> pathsFile = downloadPathsFile(path, fileIds);
        ResponseEntity<ByteArrayResource> utt2SpkFile = downloadUtt2SpkFile(fileIds);
        ResponseEntity<ByteArrayResource> spk2UttFile = downloadSpk2UttFile(fileIds);
        ResponseEntity<ByteArrayResource> segmentsFile = downloadFileForSegmentsDescription(path, fileIds);

        addToZip(zipOutputStream, "transcriptions.txt", transcriptionsFile.getBody().getByteArray());
        addToZip(zipOutputStream, "wav.scp", pathsFile.getBody().getByteArray());
        addToZip(zipOutputStream, "utt2spk.txt", utt2SpkFile.getBody().getByteArray());
        addToZip(zipOutputStream, "spk2utt.txt", spk2UttFile.getBody().getByteArray());
        addToZip(zipOutputStream, "segments.txt", segmentsFile.getBody().getByteArray());

        zipOutputStream.close();
        byte[] zipContent = zipOutput.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(zipContent.length);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=FileForESPnet.zip");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ByteArrayResource(zipContent));
    }

    private void addToZip(ZipOutputStream zipOutputStream, String fileName, byte[] content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content);
        zipOutputStream.closeEntry();
    }

}
