package com.device.api.unit;

import com.device.api.ApiApplication;
import com.device.api.model.DeviceType;
import com.device.api.model.response.RegisterResponse;
import com.device.api.services.DeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class DeviceServiceTests {

    @Autowired
    private DeviceService deviceService;

    @Test
    public void cannotRegisterInvalidMacAddress() {
        RegisterResponse response = deviceService.registerDevice(DeviceType.GATEWAY, "adsga", null);
        assertFalse(response.getSuccess());
        assertTrue(response.getErrors().contains("adsga is not a valid MAC address!"));
    }

    public void cannotRegisterNonExistentUplinkDevice() {
        RegisterResponse response = deviceService.registerDevice(DeviceType.GATEWAY, "ca:22:16:2f:1b:0f", "02:31:4e:fd:9c:a6");
        assertFalse(response.getSuccess());
        assertTrue(response.getErrors().contains("adsga is not a valid MAC address!"));
    }

}
