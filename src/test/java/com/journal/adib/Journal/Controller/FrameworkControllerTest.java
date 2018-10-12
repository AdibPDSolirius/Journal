package com.journal.adib.Journal.Controller;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.controllers.FrameworkController;
import com.journal.adib.Journal.errorHandling.ErrorHandler;
import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Framework;
import com.journal.adib.Journal.services.FrameworkService;
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
public class FrameworkControllerTest {
    private String baseURL = "/api/frameworks/";

    private MockMvc mockMvc;

    @InjectMocks
    FrameworkController frameworkController;

    @Mock
    FrameworkService frameworkServiceMock;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(frameworkController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(frameworkServiceMock.findById(1L)).thenThrow(new JournalException("Framework not found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get(baseURL + "{frameworkId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Framework not found", result.getResponse().getContentAsString());

        verify(frameworkServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void findById_Found_ShouldReturnFoundFrameworkEntry() throws Exception {
        Framework f1 = new Framework();
        f1.setName("Framework1");
        f1.setId(new Long(1));


        when(frameworkServiceMock.findById(1L)).thenReturn(f1);

        mockMvc.perform(get(baseURL + "{frameworkId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Framework1")));

        verify(frameworkServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204() throws Exception {
        when(frameworkServiceMock.findAll()).thenThrow(new JournalException("No frameworks found", HttpStatus.NO_CONTENT));

        MvcResult result = mockMvc.perform(get(baseURL))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), "No frameworks found");

        verify(frameworkServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void findAll_Found_ShouldReturnFoundFrameworkEntries() throws Exception {
        Framework f1 = new Framework();
        f1.setName("Framework1");
        f1.setId(new Long(1));

        Framework f2 = new Framework();
        f2.setName("Framework2");
        f2.setId(new Long(2));


        when(frameworkServiceMock.findAll()).thenReturn(Arrays.asList(f1, f2));


        mockMvc.perform(get(baseURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Framework1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Framework2")));

        verify(frameworkServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void create_Created_ShouldReturnCreatedFramework() throws Exception {
        Framework f1 = new Framework();
        f1.setName("Framework1");
        f1.setId(new Long(1));


        when(frameworkServiceMock.save(any(Framework.class))).thenReturn(f1);


        mockMvc.perform(post(baseURL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(f1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Framework1")));

        verify(frameworkServiceMock, times(1)).save(any(Framework.class));
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void create_NotCreated_ShouldReturnHttpStatusCode500() throws Exception {
        Framework f1 = new Framework();
        f1.setName("Framework1");
        f1.setId(new Long(1));

        when(frameworkServiceMock.save(any(Framework.class))).thenThrow(new JournalException("Failed to create new framework", HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult result = mockMvc.perform(post(baseURL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(f1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to create new framework", result.getResponse().getContentAsString());

        verify(frameworkServiceMock, times(1)).save(any(Framework.class));
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void create_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Framework f1 = new Framework();
        f1.setName(TestUtil.createStringWithLength(31));
        f1.setId(new Long(1));


        mockMvc.perform(post(baseURL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(f1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(frameworkServiceMock);
    }

    @Test
    public void update_Updated_ShouldReturnUpdatedFramework() throws Exception {
        Framework f1 = new Framework();
        f1.setName("Framework1");
        f1.setId(new Long(1));

        when(frameworkServiceMock.findById(1L)).thenReturn(f1);
        when(frameworkServiceMock.save(any(Framework.class))).thenReturn(f1);


        mockMvc.perform(put(baseURL + "{frameworkId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(f1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Framework1")));

        verify(frameworkServiceMock, times(1)).findById(1L);
        verify(frameworkServiceMock, times(1)).save(any(Framework.class));
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void update_IdNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        Framework f1 = new Framework();
        f1.setName("Framework1");
        f1.setId(new Long(1));

        when(frameworkServiceMock.findById(1L)).thenThrow(new JournalException("No framework found", HttpStatus.NOT_FOUND));


        MvcResult result = mockMvc.perform(put(baseURL + "{frameworkId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(f1)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("No framework found", result.getResponse().getContentAsString());

        verify(frameworkServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void update_NotUpdated_ShouldReturnHttpStatusCode500() throws Exception {
        Framework f1 = new Framework();
        f1.setName("Framework1");
        f1.setId(new Long(1));


        when(frameworkServiceMock.findById(1L)).thenReturn(f1);
        when(frameworkServiceMock.save(any(Framework.class))).thenThrow(new JournalException("Failed to update new framework", HttpStatus.INTERNAL_SERVER_ERROR));


        MvcResult result = mockMvc.perform(put(baseURL + "{frameworkId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(f1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to update new framework", result.getResponse().getContentAsString());

        verify(frameworkServiceMock, times(1)).findById(1L);
        verify(frameworkServiceMock, times(1)).save(any(Framework.class));
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void update_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Framework f1 = new Framework();
        f1.setName(TestUtil.createStringWithLength(31));
        f1.setId(new Long(1));


        mockMvc.perform(put(baseURL + "{frameworkId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(f1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(frameworkServiceMock);
    }
}
