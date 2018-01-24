package com.github.cxt.springmvc.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


//ClassPathScanningCandidateComponentProvider  287
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestDemoControllerTest {

	
	@Autowired
    private MockMvc mockMvc;
	
    @Test
    public void test() throws Exception {
    	MvcResult mvcResult = null;
    	MockHttpServletResponse response = null;
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rest").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"aaa\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        response = mvcResult.getResponse();
        System.out.println(response.getContentAsString());
        
        
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rest/aaa"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        response = mvcResult.getResponse();
        System.out.println(response.getContentAsString());
        
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/rest/aaa").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"aaa\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        response = mvcResult.getResponse();
        System.out.println(response.getContentAsString());
        
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/rest/aaa"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        response = mvcResult.getResponse();
        System.out.println(response.getContentAsString());
        
       
    }
}
