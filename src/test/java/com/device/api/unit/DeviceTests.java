package com.device.api.unit;

import com.device.api.model.Device;
import com.device.api.model.DeviceType;
import com.device.api.util.DeviceTopologyUtil;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceTests {

    @Test
    public void deviceEqualityTest() {
        Device device1 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        Device device2 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        assertEquals(device2, device1);
    }

    @Test
    public void equalDeviceHashCodesMatch() {
        Device device1 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        Device device2 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        assertEquals(device2.hashCode(), device1.hashCode());
    }

    @Test
    public void devicesHaveUplinkReferences() {
        Device device1 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        Device device2 = new Device("1DEA.8FD4.CC0B", DeviceType.SWITCH, device1);
        Device device3 = new Device("E9-45-A1-BD-81-41", DeviceType.ACCESS_POINT, device2);

        assertEquals(1, device1.getDownlinkDevices().size());
        assertTrue(device1.getDownlinkDevices().contains(device2));

        assertEquals(1, device2.getDownlinkDevices().size());
        assertTrue(device2.getDownlinkDevices().contains(device3));

        assertEquals(0, device3.getDownlinkDevices().size());
    }

    @Test
    public void fullTopologyTest() {
        Device rootDevice1 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        Device device2 = new Device("1DEA.8FD4.CC0B", DeviceType.SWITCH, rootDevice1);
        Device device3 = new Device("E9-45-A1-BD-81-41", DeviceType.ACCESS_POINT, device2);
        Device device4 = new Device("74:95:a6:4d:4b:29", DeviceType.ACCESS_POINT, device2);

        Device rootDevice2 = new Device("64:62:28:a1:75:7c", DeviceType.SWITCH);
        Device device5 = new Device("9B:C0:8E:A9:82:D8", DeviceType.ACCESS_POINT, rootDevice2);
        Device device6 = new Device("C0:14:3E:C6:09:F3", DeviceType.ACCESS_POINT, rootDevice2);

        Device rootDevice3 = new Device("a4:50:6d:1d:27:f4", DeviceType.GATEWAY);
        Device device7 = new Device("57:A7:D3:C2:50:2B", DeviceType.SWITCH, rootDevice3);
        Device device8 = new Device("38-E9-3D-31-7A-5A", DeviceType.GATEWAY, device7);
        Device device9 = new Device("25-82-E6-9C-FA-72", DeviceType.SWITCH, rootDevice3);

        String expectedTopology = "7ded.b437.ed1d\n" +
                "└── 1DEA.8FD4.CC0B\n" +
                "    ├── E9-45-A1-BD-81-41\n" +
                "    └── 74:95:a6:4d:4b:29\n" +
                "64:62:28:a1:75:7c\n" +
                "├── 9B:C0:8E:A9:82:D8\n" +
                "└── C0:14:3E:C6:09:F3\n" +
                "a4:50:6d:1d:27:f4\n" +
                "├── 57:A7:D3:C2:50:2B\n" +
                "│   └── 38-E9-3D-31-7A-5A\n" +
                "└── 25-82-E6-9C-FA-72\n";

        String actualTopology = DeviceTopologyUtil.getFullDeviceTopology(Arrays.asList(rootDevice1, rootDevice2, rootDevice3));
        assertEquals(expectedTopology, actualTopology);
    }

    @Test
    public void partialTopologyTest() {
        Device rootDevice = new Device("a4:50:6d:1d:27:f4", DeviceType.GATEWAY);
        Device device1 = new Device("57:A7:D3:C2:50:2B", DeviceType.SWITCH, rootDevice);
        Device device2 = new Device("38-E9-3D-31-7A-5A", DeviceType.GATEWAY, device1);
        Device device3 = new Device("25-82-E6-9C-FA-72", DeviceType.SWITCH, device2);
        Device device4 = new Device("9B:C0:8E:A9:82:D8", DeviceType.GATEWAY, device1);
        Device device5 = new Device("74:95:a6:4d:4b:29", DeviceType.ACCESS_POINT, device4);

        String expectedTopology = "57:A7:D3:C2:50:2B\n" +
                "├── 38-E9-3D-31-7A-5A\n" +
                "│   └── 25-82-E6-9C-FA-72\n" +
                "└── 9B:C0:8E:A9:82:D8\n" +
                "    └── 74:95:a6:4d:4b:29\n";
        String actualTopology = DeviceTopologyUtil.getDeviceTopology(device1);
        assertEquals(expectedTopology, actualTopology);
    }

}
