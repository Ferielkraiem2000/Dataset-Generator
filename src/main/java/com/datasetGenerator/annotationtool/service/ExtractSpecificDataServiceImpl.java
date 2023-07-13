package com.datasetGenerator.annotationtool.service;

import com.datasetGenerator.annotationtool.repository.ExtractSpecificDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractSpecificDataServiceImpl implements ExtractSpecificDataService {

    private final FileExtractContentServiceImpl fileExtractContentService;

    public ExtractSpecificDataServiceImpl(FileExtractContentServiceImpl fileExtractContentService) {
        this.fileExtractContentService = fileExtractContentService;
    }

    public ResponseEntity<String> extractSpecificData(MultipartFile file) throws IOException {
        ResponseEntity<String> response = fileExtractContentService.readFileContent(file);
        String fileContent = response.getBody();

        // Extract specific data from the file content
        List<String> extractedData = new ArrayList<>();

        // Split the file content into lines
        String[] lines = fileContent.split("\n");

        // Iterate through each line and extract the desired data
        // Iterate through each line and extract the desired data
        for (String line : lines) {
            if (!line.startsWith(";;") ) {
                String[] words = line.split(" ");
                for (String word : words) {
                    if(!word.contains("<o,f0,unknown>")){
                    extractedData.add(word);}
                }
            }
        }
// Return the extracted data as a concatenated string
        String data = String.join(" ", extractedData);
        System.out.println(data);

//        String données = new String("");
//
//        String[] specific_data = data.split(" ");
//        String chaine1 = null;
//        for (String chaine : specific_data) {
//            if (!chaine.contains("<o,f0,unknown>")) {
//                chaine1 = données.concat(chaine);
//
//            }
//        }
//        System.out.println(chaine1);
//        System.out.println("le nom du fichier est"+specific_data[0]);
        return ResponseEntity.ok(data);

    }


    }


