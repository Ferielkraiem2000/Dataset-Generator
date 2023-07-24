package com.datasetgenerator.annotationtool;

import com.datasetgenerator.annotationtool.service.FileExtractContentService;
import com.datasetgenerator.annotationtool.service.FileParseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FileParseServiceImplTest {

    FileParseServiceImpl fileParseService;
    FileExtractContentService fileExtractContentService;

    @BeforeEach
    void initTest() {
        System.out.println("appel avant chaque test");
        fileExtractContentService = mock(FileExtractContentService.class);
        this.fileParseService = new FileParseServiceImpl(fileExtractContentService, null, null, null, 7);
    }

    @Test
    void testextractValidLines1() throws IOException {
        String fileContent = "Ghadames_a_crossing_into_the_future_Raied_Gheblawi_TEDxMisurata.wav 1 Ghadames_a_crossing_into_the_future_Raied_Gheblawi_TEDxMisurata.wav_Raied_Gheblawi 17.344 26.264 <o,f0,unknown> ماتوقعوش الواقع اللي حني صايرلنا حاليا توقعوا إنّ الوضع حيكون أقل من هكي لكن ح#الوضع اللي حني فيه حاليا سيّئ جدًّا\nGhadames_a_crossing_into_the_future_Raied_Gheblawi_TEDxMisurata.wav 1 inter_segment_gap 26.264 27.706 <o,f0,>\nGhadames_a_crossing_into_the_future_Raied_Gheblawi_TEDxMisurata.wav 1 Ghadames_a_crossing_into_the_future_Raied_Gheblawi_TEDxMisurata.wav_Raied_Gheblawi 27.706 32.679 <o,f0,unknown> طبعا بالنسبة لبلاد زي ليبيااللي هي تقع في منطفة حارة";
        MockMultipartFile file = new MockMultipartFile("file1", fileContent.getBytes());
        when(fileExtractContentService.readFileContent(file))
                .thenReturn(ResponseEntity.ok().body(fileContent));
        ResponseEntity<List<String>> response = this.fileParseService.extractValidLines(file);
        List<String> validLines = response.getBody();
        assertEquals(2, validLines.size());
    }

    @Test
    void testExtractValidLines2() throws IOException {
        String fileContent = ";; Transcriber conversion by parsetrs v0.74 on 10:59:17 2022/01/14 with encoding UTF-8";
        MockMultipartFile file = new MockMultipartFile("file2", fileContent.getBytes());
        when(fileExtractContentService.readFileContent(file))
                .thenReturn(ResponseEntity.ok().body(fileContent));
        ResponseEntity<List<String>> response = this.fileParseService.extractValidLines(file);
        List<String> validLines = response.getBody();
        assertEquals(0, validLines.size());
    }

    @Test
    void testExtractValidLines3() throws IOException {
        String fileContent = ";; LABEL \"unknown\"   \"Unknown\" \"\"\n" +
                "Ghadames_a_crossing_into_the_future_Raied_Gheblawi_TEDxMisurata 1 inter_segment_gap 0.000 17.344 <o,f0,> \n" +
                "Ghadames_a_crossing_into_the_future_Raied_Gheblawi_TEDxMisurata 1 Ghadames_a_crossing_into_the_future_Raied_Gheblawi_TEDxMisurata_Raied_Gheblawi 17.344 26.264 <o,f0,unknown> ماتوقعوش الواقع اللي حني صايرلنا حاليا توقعوا إنّ الوضع حيكون أقل من هكي لكن ح#الوضع اللي حني فيه حاليا سيّئ جدًّا\n";
        MockMultipartFile file = new MockMultipartFile("file3", fileContent.getBytes());
        when(fileExtractContentService.readFileContent(file))
                .thenReturn(ResponseEntity.ok().body(fileContent));
        ResponseEntity<List<String>> response = this.fileParseService.extractValidLines(file);
        List<String> validLines = response.getBody();
        assertEquals(1, validLines.size());
    }

    @AfterEach
    void endTest() {
        System.out.println("appel après chaque test");
        this.fileParseService = null;
    }
}
