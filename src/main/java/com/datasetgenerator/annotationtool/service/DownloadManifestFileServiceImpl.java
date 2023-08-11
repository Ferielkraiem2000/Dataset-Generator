package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service

public class DownloadManifestFileServiceImpl implements DownloadManifestFileService {

    SegmentRepository segmentRepository;
    ObjectMapper objectMapper;

    public DownloadManifestFileServiceImpl(SegmentRepository segmentRepository,ObjectMapper objectMapper) {
        this.segmentRepository = segmentRepository;
        this.objectMapper=objectMapper;
    }

    public String createCombinedManifestInJsonFormat(String path, List<Long> fileIds) {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Long fileId : fileIds) {
            boolean fileIdExists = segments.stream().anyMatch(segment -> segment.getFile().getFile_id().equals(fileId));
            if (!fileIdExists) {
                throw new IllegalArgumentException("fileId " + fileId + " doesn't exist!");
            }
        }
        for (Segment segment : segments) {
            if (fileIds.contains(segment.getFile().getFile_id())) {
                filteredSegments.add(segment);
            }
        }
        Map<Long, Map<String, String>> combinedManifest = new LinkedHashMap<>();
        for (Segment segment : filteredSegments) {
            Long segmentId = segment.getSegment_id();
            String fileName = segment.getFile().getFile_name();
            Map<String, String> combinedEntry = new LinkedHashMap<>();
            if(path==null){
            combinedEntry.put("path", fileName);}
            else {
                combinedEntry.put("path", path + "/" + fileName);
            }
            combinedEntry.put("start", String.valueOf(segment.getSegment_start()));
            combinedEntry.put("stop", String.valueOf(segment.getSegment_end()));
            combinedEntry.put("duration", String.valueOf(segment.getDuration()));
            combinedEntry.put("speaker", segment.getSpeaker());
            String transcription = segment.getTranscription().replace("\r", "");
            combinedEntry.put("transcription", transcription);
            combinedManifest.put(segmentId, combinedEntry);
        }
        String combinedManifestJson;
        try {
            combinedManifestJson = objectMapper.writeValueAsString(combinedManifest);
        } catch (Exception e) {
            e.printStackTrace();
            combinedManifestJson = "{}";
        }        return combinedManifestJson;
    }

    public String createCombinedManifestInCsvFormat(String path, List<Long> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        String wav_id="";
        for (Long fileId : fileIds) {
            boolean fileIdExists = segments.stream().anyMatch(segment -> segment.getFile().getFile_id().equals(fileId));
            if (!fileIdExists) {
                throw new IllegalArgumentException("fileId " + fileId + " doesn't exist!");
            }
        }
        for (Segment segment : segments) {
            if (fileIds.contains(segment.getFile().getFile_id())) {
                filteredSegments.add(segment);
            }
        }
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(new String[]{"ID", "path", "start", "stop", "duration", "transcription", "speaker"});
        for (Segment segment : filteredSegments) {
            Long segmentId = segment.getSegment_id();
            String fileName = segment.getFile().getFile_name();
            if(path==null){ wav_id=fileName;}
            else{
            Path concatenatedPath = Paths.get(path, fileName);
             wav_id = concatenatedPath.toString();}
            String transcription = segment.getTranscription();

            String[] rowData = new String[]{
                    String.valueOf(segmentId),
                    wav_id,
                    String.valueOf(segment.getSegment_start()),
                    String.valueOf(segment.getSegment_end()),
                    String.valueOf(segment.getDuration()),
                    transcription,
                    segment.getSpeaker()
            };
            csvWriter.writeNext(rowData);
        }
        csvWriter.close();
        return writer.toString();
    }

    private ByteArrayResource createTextFileForTranscriptions(List<Long> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Long fileId : fileIds) {
            boolean fileIdExists = segments.stream().anyMatch(segment -> segment.getFile().getFile_id().equals(fileId));
            if (!fileIdExists) {
                throw new IllegalArgumentException("fileId " + fileId + " doesn't exist!");
            }
        }
        for (Segment segment : segments) {
            if (fileIds.contains(segment.getFile().getFile_id())) {
                filteredSegments.add(segment);
            }
        }
        Path filePath = Files.createTempFile("transcriptions", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Segment segment : filteredSegments) {
            Long segment_id = segment.getSegment_id();
            String transcription = segment.getTranscription();
            writer.write(segment_id + " " + transcription);
            writer.newLine();
        }
        writer.close();

        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }

    private ByteArrayResource createUtt2SpkFile(List<Long> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Long fileId : fileIds) {
            boolean fileIdExists = segments.stream().anyMatch(segment -> segment.getFile().getFile_id().equals(fileId));
            if (!fileIdExists) {
                throw new IllegalArgumentException("fileId " + fileId + " doesn't exist!");
            }
        }
        for (Segment segment : segments) {
            if (fileIds.contains(segment.getFile().getFile_id())) {
                filteredSegments.add(segment);
            }
        }
        Path filePath = Files.createTempFile("utt2spk", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Segment segment : filteredSegments) {
            Long segmentId = segment.getSegment_id();
            String speaker = segment.getSpeaker();
            writer.write(segmentId + " " + speaker);
            writer.newLine();
        }
        writer.close();
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }

    private ByteArrayResource createSpk2UttFile(List<Long> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        for (Long fileId : fileIds) {
            boolean fileIdExists = segments.stream().anyMatch(segment -> segment.getFile().getFile_id().equals(fileId));
            if (!fileIdExists) {
                throw new IllegalArgumentException("fileId " + fileId + " doesn't exist!");
            }
        }
        for (Segment segment : segments) {
            if (fileIds.contains(segment.getFile().getFile_id())) {
                filteredSegments.add(segment);
            }
        }
        Map<String, List<Long>> speakerToSegmentIdsMap = new HashMap<>();

        for (Segment segment : filteredSegments) {
            String speaker = segment.getSpeaker();
            speakerToSegmentIdsMap.putIfAbsent(speaker, new ArrayList<>());
            speakerToSegmentIdsMap.get(speaker).add(segment.getSegment_id());
        }
        Path filePath = Files.createTempFile("spk2utt", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Map.Entry<String, List<Long>> entry : speakerToSegmentIdsMap.entrySet()) {
            String speaker = entry.getKey();
            List<Long> segmentIds = entry.getValue();
            writer.write(speaker + " " + segmentIds);
            writer.newLine();
        }
        writer.close();
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }

    private ByteArrayResource createPathsFile(String path, List<Long> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        String wav_id="";
        for (Long fileId : fileIds) {
            boolean fileIdExists = segments.stream().anyMatch(segment -> segment.getFile().getFile_id().equals(fileId));
            if (!fileIdExists) {
                throw new IllegalArgumentException("fileId " + fileId + " doesn't exist!");
            }
        }
        for (Segment segment : segments) {
            if (fileIds.contains(segment.getFile().getFile_id())) {
                filteredSegments.add(segment);
            }
        }
        Path filePath = Files.createTempFile("wav", ".scp");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        List<String> segmentIds = new ArrayList<>();
        for (Segment segment : filteredSegments) {
            Long segment_id = segment.getSegment_id();
            String file_name = String.valueOf(segment.getFile().getFile_name());
            if(path==null){wav_id=file_name;}
          else{  Path concatenatedPath = Paths.get(path, file_name);
            wav_id = concatenatedPath.toString();}
            writer.write(segment_id + " " + wav_id);
            writer.newLine();
        }
        writer.close();
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }


    private ByteArrayResource createFileForSegmentsDescription(String path, List<Long> fileIds) throws IOException {
        List<Segment> segments = segmentRepository.findAll();
        List<Segment> filteredSegments = new ArrayList<>();
        String wav_id="";
        for (Long fileId : fileIds) {
            boolean fileIdExists = segments.stream().anyMatch(segment -> segment.getFile().getFile_id().equals(fileId));
            if (!fileIdExists) {
                throw new IllegalArgumentException("fileId " + fileId + " doesn't exist!");
            }
        }
        for (Segment segment : segments) {
            if (fileIds.contains(segment.getFile().getFile_id())) {
                filteredSegments.add(segment);
            }
        }
        Path filePath = Files.createTempFile("segments", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        for (Segment segment : filteredSegments) {
            Long utterance_id = segment.getSegment_id();
            String file_name = segment.getFile().getFile_name();
            if(path==null){wav_id=file_name;}
           else{ Path concatenatedPath = Paths.get(path, file_name);
            wav_id = concatenatedPath.toString();}
            double start_time = segment.getSegment_start();
            double end_time = segment.getSegment_end();
            writer.write(utterance_id + " " + wav_id + " " + start_time + " " + end_time);
            writer.newLine();
        }
        writer.close();
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }

    public ByteArrayResource createFileCompatibleWithESPnet(String path, List<Long> fileIds) throws IOException {
        ByteArrayOutputStream zipOutput = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(zipOutput);

        ByteArrayResource transcriptionsFile = createTextFileForTranscriptions(fileIds);
        ByteArrayResource pathsFile = createPathsFile(path, fileIds);
        ByteArrayResource utt2SpkFile = createUtt2SpkFile(fileIds);
        ByteArrayResource spk2UttFile = createSpk2UttFile(fileIds);
        ByteArrayResource segmentsFile = createFileForSegmentsDescription(path, fileIds);

        addToZip(zipOutputStream, "transcriptions.txt", transcriptionsFile.getByteArray());
        addToZip(zipOutputStream, "wav.scp", pathsFile.getByteArray());
        addToZip(zipOutputStream, "utt2spk.txt", utt2SpkFile.getByteArray());
        addToZip(zipOutputStream, "spk2utt.txt", spk2UttFile.getByteArray());
        addToZip(zipOutputStream, "segments.txt", segmentsFile.getByteArray());

        zipOutputStream.close();
        byte[] zipContent = zipOutput.toByteArray();
        return new ByteArrayResource(zipContent);
    }

    private void addToZip(ZipOutputStream zipOutputStream, String fileName, byte[] content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content);
        zipOutputStream.closeEntry();
    }

    public ByteArrayResource createCombinedManifest(String format, String path, List<Long> fileIds) throws IOException {

        if (format.equalsIgnoreCase("JSON")) {
            return new ByteArrayResource(createCombinedManifestInJsonFormat(path, fileIds).getBytes());
        } else if (format.equalsIgnoreCase("CSV")) {
            return new ByteArrayResource(createCombinedManifestInCsvFormat(path, fileIds).getBytes());
        } else if (format.equalsIgnoreCase("ESPnet")) {
            return createFileCompatibleWithESPnet(path, fileIds);
        } else {
            throw new IllegalArgumentException("Format: " + format + " not supported!");
        }
    }
}
