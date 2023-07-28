package com.datasetgenerator.annotationtool.service;

import com.datasetgenerator.annotationtool.model.Segment;
import com.datasetgenerator.annotationtool.repository.SegmentRepository;
import com.google.gson.Gson;
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

    public DownloadManifestFileServiceImpl(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    public String createCombinedManifestInJsonFormat(String path, List<String> fileIds) {
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
        return combinedManifestJson;
    }

    public String createCombinedManifestInCsvFormat(String path, List<String> fileIds) throws IOException {
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
            Path concatenatedPath = Paths.get(path, fileName);
            String wav_id = concatenatedPath.toString();
            String transcription = segment.getTranscription();

            String[] rowData = new String[]{
                    segmentId,
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

    private ByteArrayResource createTextFileForTranscriptions(List<String> fileIds) throws IOException {
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

        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }


    private ByteArrayResource createUtt2SpkFile(List<String> fileIds) throws IOException {
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
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }

    private ByteArrayResource createSpk2UttFile(List<String> fileIds) throws IOException {
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
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }


    private ByteArrayResource createPathsFile(String path, List<String> fileIds) throws IOException {
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
            Path concatenatedPath = Paths.get(path, file_name);
            String wav_id = concatenatedPath.toString();
            writer.write(segment_id + " " + wav_id);
            writer.newLine();
        }
        writer.close();
        byte[] content = Files.readAllBytes(filePath);
        Files.delete(filePath);
        return new ByteArrayResource(content);
    }


    private ByteArrayResource createFileForSegmentsDescription(String path, List<String> fileIds) throws IOException {
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
            Path concatenatedPath = Paths.get(path, file_name);
            String wav_id = concatenatedPath.toString();
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

    public ByteArrayResource createFileCompatibleWithESPnet(String path, List<String> fileIds) throws IOException {
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

}
