package com.journal.adib.Journal.Controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ResourceController resourceController;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testResourceController() throws Exception {
        
    }

}