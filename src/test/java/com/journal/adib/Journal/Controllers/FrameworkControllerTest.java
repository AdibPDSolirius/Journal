package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.ErrorHandling.ErrorHandler;
import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Models.Framework;
import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Models.Library;
import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Services.FrameworkService;
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
public class FrameworkControllerTest {

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

        MvcResult result = mockMvc.perform(get("/frameworks/{frameworkId}", 1L))
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

        mockMvc.perform(get("/frameworks/{frameworkId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Framework1")));

        verify(frameworkServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(frameworkServiceMock);
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204() throws Exception {
        when(frameworkServiceMock.findAll()).thenThrow(new JournalException("No frameworks found", HttpStatus.NO_CONTENT));

        MvcResult result = mockMvc.perform(get("/frameworks"))
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


        mockMvc.perform(get("/frameworks"))
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
    public void create_Created_ShouldReturnFrameworkResource() throws Exception {
        Framework f1 = new Framework();
        f1.setName("Framework1");
        f1.setId(new Long(1));


        when(frameworkServiceMock.save(any(Framework.class))).thenReturn(f1);


        mockMvc.perform(post("/frameworks")
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

        MvcResult result = mockMvc.perform(post("/frameworks")
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


        mockMvc.perform(post("/frameworks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(f1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(frameworkServiceMock);
    }
}
