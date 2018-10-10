package com.journal.adib.Journal.Controller;

import com.journal.adib.Journal.controllers.FileController;
import com.journal.adib.Journal.errorHandling.ErrorHandler;
import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.services.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileControllerTest{

    private MockMvc mockMvc;

    @InjectMocks
    private FileController fileController;

    @Mock
    private StorageService storageServiceMock;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(fileController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void uploadFile_Success_ShouldReturnFileName() throws Exception {

    }

    @Test
    public void findByName_NotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(storageServiceMock.getFile("file.jpeg")).thenThrow(new JournalException("File not found", HttpStatus.NOT_FOUND));
        MvcResult result = mockMvc.perform(get("/api/files/{filename:.*}", "file.jpeg"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("File not found", result.getResponse().getContentAsString());

        verify(storageServiceMock, times(1)).getFile("file.jpeg");
        verifyNoMoreInteractions(storageServiceMock);
    }

    @Test
    public void findByName_NotCorrectType_ShouldReturnHttpStatusCode415() throws Exception {
        when(storageServiceMock.getFile("file.abc")).thenThrow(new JournalException("Not correct file type", HttpStatus.UNSUPPORTED_MEDIA_TYPE));
        MvcResult result = mockMvc.perform(get("/api/files/{filename:.*}", "file.abc"))
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();
        assertEquals("Not correct file type", result.getResponse().getContentAsString());
        verify(storageServiceMock, times(1)).getFile("file.jpeg");
        verifyNoMoreInteractions(storageServiceMock);
    }

    @Test
    public void uploadFile_Success_ShouldReturnFilename() throws Exception{
        String mockName = "file";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(mockName, mockName.getBytes());
        when(storageServiceMock.store(mockMultipartFile)).thenReturn("123456file.jpeg");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload").file(mockMultipartFile))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("123456file.jpeg", result.getResponse().getContentAsString());
        verify(storageServiceMock, times(1)).store(mockMultipartFile);
        verifyNoMoreInteractions(storageServiceMock);
    }

    @Test
    public void uploadFile_Failed_ShouldReturnHttpStatusCode415() throws Exception{
        String mockName = "file";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(mockName, mockName.getBytes());
        when(storageServiceMock.store(mockMultipartFile)).thenThrow(new JournalException("Not correct file type", HttpStatus.UNSUPPORTED_MEDIA_TYPE));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload").file(mockMultipartFile))
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();
        assertEquals("Not correct file type", result.getResponse().getContentAsString());
        verify(storageServiceMock, times(1)).store(mockMultipartFile);
        verifyNoMoreInteractions(storageServiceMock);

    }
}
