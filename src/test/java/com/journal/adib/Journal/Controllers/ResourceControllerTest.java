//package com.journal.adib.Journal.Controllers;
//
//import com.journal.adib.Journal.Models.Resource;
//import com.journal.adib.Journal.Services.ResourceService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.when;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//public class ResourceControllerTest {
//
//
//    @Mock
//    ResourceService resourceServiceMock;
//
//    @InjectMocks
//    private ResourceController resourceController;
//
//    @Before
//    public void setUp(){
//        Resource r1 = new Resource();
//        r1.setName("Resource1");
//        r1.setUrl("Resource1Url");
//        r1.setId(new Long(1));
//
//        Resource r2 = new Resource();
//        r2.setName("Resource2");
//        r2.setUrl("Resource2Url");
//        r2.setId(new Long(2));
//
//        List<Resource> rList = new ArrayList<>();
//        rList.add(r1);
//        rList.add(r2);
//
//        when(resourceServiceMock.findAll()).thenReturn(rList);
//        when(resourceServiceMock.findById(new Long(1))).thenReturn(r1);
//
//    }
//
//    @Test
//    public void testResourceControllerReturnsResourceSize() throws Exception {
//        assertEquals(2, resourceController.find().size());
//    }
//
//}