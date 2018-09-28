package com.journal.adib.Journal.Controller;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.LanguageController;
import com.journal.adib.Journal.ErrorHandling.ErrorHandler;
import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Services.LanguageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class LanguageControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    LanguageController languageController;

    @Mock
    LanguageService languageServiceMock;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(languageController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(languageServiceMock.findById(1L)).thenThrow(new JournalException("Language not found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get("/languages/{languageId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Language not found", result.getResponse().getContentAsString());

        verify(languageServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void findById_Found_ShouldReturnFoundLanguageEntry() throws Exception {
        Language l1 = new Language();
        l1.setName("Language1");
        l1.setId(new Long(1));


        when(languageServiceMock.findById(1L)).thenReturn(l1);

        mockMvc.perform(get("/languages/{languageId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Language1")));

        verify(languageServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204() throws Exception {
        when(languageServiceMock.findAll()).thenThrow(new JournalException("No languages found", HttpStatus.NO_CONTENT));

        MvcResult result = mockMvc.perform(get("/languages"))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), "No languages found");

        verify(languageServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void findAll_Found_ShouldReturnFoundLanguageEntries() throws Exception {
        Language l1 = new Language();
        l1.setName("Language1");
        l1.setId(new Long(1));

        Language l2 = new Language();
        l2.setName("Language2");
        l2.setId(new Long(2));


        when(languageServiceMock.findAll()).thenReturn(Arrays.asList(l1, l2));


        mockMvc.perform(get("/languages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Language1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Language2")));

        verify(languageServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void create_Created_ShouldReturnCreatedLanguage() throws Exception {
        Language l1 = new Language();
        l1.setId(new Long(1));
        l1.setName("Language1");

        when(languageServiceMock.save(any(Language.class))).thenReturn(l1);


        mockMvc.perform(post("/languages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Language1")));

        verify(languageServiceMock, times(1)).save(any(Language.class));
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void create_NotCreated_ShouldReturnHttpStatusCode500() throws Exception {
        Language l1 = new Language();
        l1.setId(new Long(1));
        l1.setName("Language1");

        when(languageServiceMock.save(any(Language.class))).thenThrow(new JournalException("Failed to create new language", HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult result = mockMvc.perform(post("/languages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to create new language", result.getResponse().getContentAsString());

        verify(languageServiceMock, times(1)).save(any(Language.class));
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void create_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Language l1 = new Language();
        l1.setId(new Long(1));
        l1.setName(TestUtil.createStringWithLength(31));


        mockMvc.perform(post("/languages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(languageServiceMock);
    }

    @Test
    public void update_Updated_ShouldReturnUpdatedLanguage() throws Exception {
        Language l1 = new Language();
        l1.setName("Language1");
        l1.setId(new Long(1));

        when(languageServiceMock.findById(1L)).thenReturn(l1);
        when(languageServiceMock.save(any(Language.class))).thenReturn(l1);


        mockMvc.perform(put("/languages/{languageId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Language1")));

        verify(languageServiceMock, times(1)).findById(1L);
        verify(languageServiceMock, times(1)).save(any(Language.class));
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void update_IdNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        Language l1 = new Language();
        l1.setName("Language1");
        l1.setId(new Long(1));

        when(languageServiceMock.findById(1L)).thenThrow(new JournalException("No language found", HttpStatus.NOT_FOUND));


        MvcResult result = mockMvc.perform(put("/languages/{languageId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("No language found", result.getResponse().getContentAsString());

        verify(languageServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void update_NotUpdated_ShouldReturnHttpStatusCode500() throws Exception {
        Language l1 = new Language();
        l1.setName("Language1");
        l1.setId(new Long(1));


        when(languageServiceMock.findById(1L)).thenReturn(l1);
        when(languageServiceMock.save(any(Language.class))).thenThrow(new JournalException("Failed to update new language", HttpStatus.INTERNAL_SERVER_ERROR));


        MvcResult result = mockMvc.perform(put("/languages/{languageId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to update new language", result.getResponse().getContentAsString());

        verify(languageServiceMock, times(1)).findById(1L);
        verify(languageServiceMock, times(1)).save(any(Language.class));
        verifyNoMoreInteractions(languageServiceMock);
    }

    @Test
    public void update_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Language l1 = new Language();
        l1.setId(new Long(1));
        l1.setName(TestUtil.createStringWithLength(31));


        mockMvc.perform(put("/languages/{languageId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(languageServiceMock);
    }
}
