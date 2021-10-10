package com.device.api.unit;

import com.device.api.util.MacAddressUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MacAddressUtilTests {

    @Test
    public void macAddressUtilTest(){
        String[] validAddresses = { "BA:D7:53:AC:F6:1B", "AB:59:49:17:9C:A8", "f7:16:b6:68:22:0f", "ca:22:16:2f:1b:0f",
                                    "E9-45-A1-BD-81-41", "F1-43-A5-0A-0F-FF", "0d-b0-69-15-f5-71", "ad-d6-dc-08-b9-7c",
                                    "D2B4.AF38.7E57", "1DEA.8FD4.CC0B",  "6426.cdf7.033f", "7ded.b437.ed1d" };
        String[] invalidAddresses = { "", " ", "a1", "a1f2fq31a31421", "kkasd", "1", "531t1331sf", "7ded:b437,ed1d",
                                    "ad.d6.dc.08.b9.7c", "6426-cdf7-033f"};

        for(String macAddress : validAddresses) {
            assertTrue(MacAddressUtil.isValidMacAddress(macAddress));
        }
        for(String macAddress : invalidAddresses) {
            assertFalse(MacAddressUtil.isValidMacAddress(macAddress));
        }
    }
}
