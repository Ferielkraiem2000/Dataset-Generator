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
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service

public class DownloadManifestFileServiceImpl implements DownloadManifestFileService {

    SegmentRepository segmentRepository;

    public DownloadManifestFileServiceImpl(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    public ResponseEntity<String> downloadCombinedManifestInJsonFormat() {
        List<Segment> segments = segmentRepository.findAll();
        Map<String, Map<String, String>> combinedManifest = new LinkedHashMap<>();
        for (Segment segment : segments) {
            String segmentId = String.valueOf(segment.getSegment_id());
            String fileName = segment.getFile().getFile_name();
            Map<String, String> combinedEntry = new LinkedHashMap<>();
            combinedEntry.put("path", "path/to/wav/" + fileName);
            combinedEntry.put("transcription", segment.getTranscription());
            combinedEntry.put("start", String.valueOf(segment.getSegment_start()));
            combinedEntry.put("stop", String.valueOf(segment.getSegment_end()));
            combinedEntry.put("duration", String.valueOf(segment.getDuration()));
            combinedEntry.put("speaker", segment.getSpeaker());
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

    public ResponseEntity<String> downloadCombinedManifestInCsvFormat() throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(new String[]{"ID", "path", "start", "stop", "duration", "transcription", "speaker"});
        for (Segment segment : segments) {
            String segmentId = String.valueOf(segment.getSegment_id());
            String fileName = segment.getFile().getFile_name();
            String escapedTranscription = HtmlUtils.htmlEscape(segment.getTranscription());
            String[] rowData = new String[]{
                    segmentId,
                    "path/to/wav/" + fileName,
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

    public ResponseEntity<ByteArrayResource> downloadTextFileForTranscriptions() throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        Path filePath = Files.createTempFile("transcriptions", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Segment segment : segments) {
            String transcription = segment.getTranscription();
            writer.write(transcription);
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


    public ResponseEntity<ByteArrayResource> downloadUtt2SpkFile() throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        Path filePath = Files.createTempFile("utt2spk", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));

        for (Segment segment : segments) {
            String segmentId = String.valueOf(segment.getSegment_id());
            String speaker = segment.getSpeaker();
            writer.write("segment_id=" + segmentId + " " + "speaker=" + speaker);
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

    public ResponseEntity<ByteArrayResource> downloadSpk2UttFile() throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        Path filePath = Files.createTempFile("spk2utt", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));

        for (Segment segment : segments) {
            String segmentId = String.valueOf(segment.getSegment_id());
            String speaker = segment.getSpeaker();
            writer.write("speaker=" + speaker + " " + "segment_id=" + segmentId);
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

    public ResponseEntity<ByteArrayResource> downloadFileForSegmentsDescription() throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        Path filePath = Files.createTempFile("segments", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));

        for (Segment segment : segments) {
            String utterance_id = String.valueOf(segment.getSegment_id());
            Long wav_id = segment.getFile().getFile_id();
            double start_time = segment.getSegment_start();
            double end_time = segment.getSegment_end();
            writer.write("utterance_id =" + utterance_id + " " + "wav_id =" + wav_id + " " + " start_time =" + start_time + " " + "end_time =" + end_time);
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

    public ResponseEntity<ByteArrayResource> downloadFileCompatibleWithESPnet() throws IOException {
        ByteArrayOutputStream zipOutput = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(zipOutput);

        ResponseEntity<ByteArrayResource> transcriptionsFile = downloadTextFileForTranscriptions();
        ResponseEntity<ByteArrayResource> utt2SpkFile = downloadUtt2SpkFile();
        ResponseEntity<ByteArrayResource> spk2UttFile = downloadSpk2UttFile();
        ResponseEntity<ByteArrayResource> segmentsFile = downloadFileForSegmentsDescription();

        addToZip(zipOutputStream, "transcriptions.txt", transcriptionsFile.getBody().getByteArray());
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
