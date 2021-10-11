package com.device.api.integration;

import com.device.api.exception.ApiRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
public class DeviceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findByNonExistentMacAddressReturnsNotFound() throws Exception {
        mockMvc.perform(get("/device/findByMacAddress?macAddress=gagas1421"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException));
    }

    @Test
    public void canRegisterAndFindDevice() throws Exception {
        mockMvc.perform(post("/device/register?deviceType=GATEWAY&macAddress=57:A7:D3:C2:50:2B"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"errors\":[],\"success\":true}"));
        mockMvc.perform(get("/device/findByMacAddress?macAddress=57:A7:D3:C2:50:2B"))
                .andExpect(status().isOk());
    }

    @Test
    public void canGetAllDevices() throws Exception {
        mockMvc.perform(get("/device"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void topologyOfNonExistentDeviceReturnsNotFound() throws Exception {
        mockMvc.perform(get("/device/topology?macAddress=gagas1421"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException));
    }

    @Test
    public void canGetPartialTopology() throws Exception {
        mockMvc.perform(post("/device/register?deviceType=GATEWAY&macAddress=fa-c0-ee-da-38-18"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"errors\":[],\"success\":true}"));
        mockMvc.perform(get("/device/topology?macAddress=fa-c0-ee-da-38-18"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void canGetFullTopology() throws Exception {
        mockMvc.perform(get("/device/topology"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
