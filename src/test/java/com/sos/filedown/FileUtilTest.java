package com.sos.filedown;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class FileUtilTest {
    @Autowired
    MockMvc mockMvc;
    
    @Test
    void ioFileDownload() throws Exception {
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/io/filedown/")).andExpect(status().isOk()).andReturn();

        long endTime = System.currentTimeMillis();
        // 소요된 시간 출력
        long elapsedTime = endTime - startTime;

        log.info("소요시간 :: {}",elapsedTime);
    }
    @Test
    void nioFileDownload() throws Exception {
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/nio/filedown/")).andExpect(status().isOk()).andReturn();

        long endTime = System.currentTimeMillis();
        // 소요된 시간 출력
        long elapsedTime = endTime - startTime;

        log.info("소요시간 :: {}",elapsedTime);
    }
}