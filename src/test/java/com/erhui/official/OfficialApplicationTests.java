package com.erhui.official;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OfficialApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
    }

    @Test
    public void logoutTest() throws Exception {
        mockMvc.perform(get("/api/user/logout").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(post("/api/user/login").accept(MediaType.APPLICATION_JSON)
                .param("username", "admin")
                .param("password", "admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void registerTest() throws Exception {
        mockMvc.perform(post("/api/user/add").accept(MediaType.APPLICATION_JSON)
                .param("username", "zhangsan")
                .param("password", "123456")
                .param("email", "111@qq.com"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

