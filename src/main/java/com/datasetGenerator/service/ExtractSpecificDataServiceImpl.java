package com.datasetGenerator.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractSpecificDataServiceImpl implements ExtractSpecificDataService {
    private final FileExtractContentService fileExtractContentService;

    public ExtractSpecificDataServiceImpl(FileExtractContentService fileExtractContentService) {
        this.fileExtractContentService = fileExtractContentService;
    }

    public ResponseEntity<String> extractSpecificData(MultipartFile file) throws IOException {

        ResponseEntity<String> response = fileExtractContentService.readFileContent(file);
        String fileContent = response.getBody();

        List<String> extractedData = new ArrayList<>();
        List<String> transcripteddData = new ArrayList<>();
        String[] lines = fileContent.split("\n");
        for (String line : lines) {
            if (!line.startsWith(";;")) {
                String[] words = line.split(" ");
                for (String word : words) {
                    if (!word.contains("<o,f0,unknown>")) {
                        extractedData.add(word);
                    }
                }
                for (int i = 0; i < words.length; i++) {
                    if (words[i].equals("<o,f0,unknown>")) {
                        for (int j = i + 1; j < words.length; j++) {
                            transcripteddData.add(words[j]);
                        }
                    }
                }
            }
        }

        String fileName = extractedData.get(0);
        String fileId = extractedData.get(1);
        String speakerName = extractedData.get(2);
        String vocalStart = extractedData.get(3);
        String vocalEnd = extractedData.get(4);
        String transcriptedData = String.join(" ", transcripteddData);

        String result = "le nom du fichier est: " + fileName +
                "\n l'id du fichier est: " + fileId +
                "\n le nom du speaker est : " + speakerName +
                "\n le début du vocal est : " + vocalStart +
                "\n la fin du vocal est :" + vocalEnd +
                "\n la donnée transcrite est :" + transcriptedData;

        System.out.println(result);
        return ResponseEntity.ok(result);
    }
}

