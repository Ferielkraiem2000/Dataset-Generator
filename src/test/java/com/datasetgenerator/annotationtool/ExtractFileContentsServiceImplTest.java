package com.datasetgenerator.annotationtool;

import com.datasetgenerator.annotationtool.service.ExtractFileContentsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtractFileContentsServiceImplTest {
    ExtractFileContentsServiceImpl fileExtractContentService;

    @BeforeEach
    void initTest() {
        System.out.println("appel avant chaque test");
        this.fileExtractContentService = new ExtractFileContentsServiceImpl();
    }

    @Test
    void testReadFileWithEmptyFile() throws IOException {
        //arrange
        MockMultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
        //act
        String actualResult = this.fileExtractContentService.readFileContent(emptyFile);
        //assert
        assertEquals(actualResult, "");
    }

    @Test
    void testReadFileWithFileWithContent() throws IOException {
        //arrange
        String fileContent = "This the content of the file";
        MockMultipartFile fileWithContent = new MockMultipartFile("fileWithContent", fileContent.getBytes());
        //act
        String actualResult = this.fileExtractContentService.readFileContent(fileWithContent);
        //assert
        assertEquals(actualResult, "This the content of the file");
    }

    @AfterEach
    public void endTest() {
        System.out.println("appel après chaque test");
        this.fileExtractContentService = null;
    }
}
