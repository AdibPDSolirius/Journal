package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.ErrorHandling.EntityNotFoundException;
import com.journal.adib.Journal.ErrorHandling.ErrorHandler;
import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Services.ResourceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ResourceControllerTest {


    private MockMvc mockMvc;

    @Mock
    ResourceService resourceServiceMock;

    @InjectMocks
    private ResourceController resourceController;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(resourceController)
                .setControllerAdvice(new ErrorHandler())
                .build();

    }

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode404() throws EntityNotFoundException, Exception {
        when(resourceServiceMock.findById(1L)).thenThrow(new EntityNotFoundException("Resource not found", HttpStatus.NOT_FOUND));

        //Checks headers
        MvcResult result = mockMvc.perform(get("/resources/{resourceId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        //Checks content body
        assertEquals(result.getResponse().getContentAsString(), "Resource not found");

        //Verifies the "findById" service only ran once
        verify(resourceServiceMock, times(1)).findById(1L);
        //Verifies no other methods in our mock service were called
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void findById_Found_ShouldReturnFoundResourceEntry() throws Exception {
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));
        when(resourceServiceMock.findById(1L)).thenReturn(r1);

        mockMvc.perform(get("/resources/{resourceId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Resource1")))
                .andExpect(jsonPath("$.url", is("Resource1Url")));

        verify(resourceServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

}