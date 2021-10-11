package com.device.api.unit;

import com.device.api.model.Device;
import com.device.api.model.DeviceType;
import com.device.api.model.response.RegisterResponse;
import com.device.api.repository.InMemoryDeviceRepository;
import com.device.api.services.DeviceService;
import com.device.api.services.DeviceServiceImpl;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceServiceTests {

    @Test
    public void cannotRegisterInvalidMacAddress() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        RegisterResponse response = deviceService.registerDevice(DeviceType.GATEWAY, "adsga", null);
        assertFalse(response.getSuccess());
        assertTrue(response.getErrors().contains("adsga is not a valid MAC address!"));
    }

    @Test
    public void cannotRegisterNonExistentUplinkDevice() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        RegisterResponse response = deviceService.registerDevice(DeviceType.GATEWAY, "ca:22:16:2f:1b:0f", "02:31:4e:fd:9c:a6");
        assertFalse(response.getSuccess());
        assertTrue(response.getErrors().contains("Uplink device with MAC address 02:31:4e:fd:9c:a6 not found!"));
    }

    @Test
    public void cannotRegisterAlreadyRegisteredMacAddress() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        RegisterResponse response1 = deviceService.registerDevice(DeviceType.GATEWAY, "d1:51:ea:e8:1d:3c", null);
        assertTrue(response1.getSuccess());

        RegisterResponse response2 = deviceService.registerDevice(DeviceType.GATEWAY, "d1:51:ea:e8:1d:3c", null);
        assertFalse(response2.getSuccess());
        assertTrue(response2.getErrors().contains("Device d1:51:ea:e8:1d:3c already registered!"));
    }

    @Test
    public void cannotRegisterMacAddressSameAsUplink() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        RegisterResponse response = deviceService.registerDevice(DeviceType.GATEWAY, "67:4c:f8:d6:58:a6", "67:4c:f8:d6:58:a6");
        assertFalse(response.getSuccess());
        assertTrue(response.getErrors().contains("Device MAC address cannot be the same as uplink device!"));
    }

    @Test
    public void canRegisterValidDevices() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        RegisterResponse response1 = deviceService.registerDevice(DeviceType.GATEWAY, "8C:06:8C:A9:B6:9A", null);
        assertTrue(response1.getSuccess());
        RegisterResponse response2 = deviceService.registerDevice(DeviceType.GATEWAY, "D9:A6:EC:0C:F2:33", "8C:06:8C:A9:B6:9A");
        assertTrue(response2.getSuccess());
    }

    @Test
    public void canFindDeviceByMacAddress() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        deviceService.registerDevice(DeviceType.GATEWAY, "4F:86:03:C5:05:E6", null);
        Device resultDevice = deviceService.findByMacAddress("4F:86:03:C5:05:E6");
        assertNotNull(resultDevice);
        assertEquals("4F:86:03:C5:05:E6", resultDevice.getMacAddress());
    }

    @Test
    public void findByNonExistentMacAddressReturnsNull() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        Device searchResult = deviceService.findByMacAddress("EB:CF:B5:34:C5:38");
        assertNull(searchResult);
    }

    @Test
    public void fullTopologyWithoutDevicesShowsNoDevices() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());
        assertEquals("No devices registered!", deviceService.getFullTopology());
    }

    @Test
    public void correctFullTopology() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        deviceService.registerDevice(DeviceType.GATEWAY, "7ded.b437.ed1d", null);
        deviceService.registerDevice(DeviceType.SWITCH, "1DEA.8FD4.CC0B", "7ded.b437.ed1d");
        deviceService.registerDevice(DeviceType.ACCESS_POINT, "E9-45-A1-BD-81-41", "1DEA.8FD4.CC0B");
        deviceService.registerDevice(DeviceType.ACCESS_POINT, "74:95:a6:4d:4b:29", "1DEA.8FD4.CC0B");
        deviceService.registerDevice(DeviceType.SWITCH, "64:62:28:a1:75:7c", null);
        deviceService.registerDevice(DeviceType.ACCESS_POINT, "9B:C0:8E:A9:82:D8", "64:62:28:a1:75:7c");
        deviceService.registerDevice(DeviceType.ACCESS_POINT, "C0:14:3E:C6:09:F3", "64:62:28:a1:75:7c");
        deviceService.registerDevice(DeviceType.GATEWAY, "a4:50:6d:1d:27:f4", null);
        deviceService.registerDevice(DeviceType.SWITCH, "57:A7:D3:C2:50:2B", "a4:50:6d:1d:27:f4");
        deviceService.registerDevice(DeviceType.GATEWAY, "38-E9-3D-31-7A-5A", "57:A7:D3:C2:50:2B");
        deviceService.registerDevice(DeviceType.SWITCH, "25-82-E6-9C-FA-72", "a4:50:6d:1d:27:f4");

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

        assertEquals(expectedTopology, deviceService.getFullTopology());
    }

    @Test
    public void correctPartialTopology() {
        DeviceService deviceService = new DeviceServiceImpl(new InMemoryDeviceRepository());

        deviceService.registerDevice(DeviceType.GATEWAY, "a4:50:6d:1d:27:f4", null);
        deviceService.registerDevice(DeviceType.SWITCH, "57:A7:D3:C2:50:2B", "a4:50:6d:1d:27:f4");
        deviceService.registerDevice(DeviceType.GATEWAY, "38-E9-3D-31-7A-5A", "57:A7:D3:C2:50:2B");
        deviceService.registerDevice(DeviceType.SWITCH, "25-82-E6-9C-FA-72", "38-E9-3D-31-7A-5A");
        deviceService.registerDevice(DeviceType.GATEWAY, "9B:C0:8E:A9:82:D8", "57:A7:D3:C2:50:2B");
        deviceService.registerDevice(DeviceType.ACCESS_POINT, "74:95:a6:4d:4b:29", "9B:C0:8E:A9:82:D8");

        String expectedTopology = "57:A7:D3:C2:50:2B\n" +
                "├── 38-E9-3D-31-7A-5A\n" +
                "│   └── 25-82-E6-9C-FA-72\n" +
                "└── 9B:C0:8E:A9:82:D8\n" +
                "    └── 74:95:a6:4d:4b:29\n";

        Device device = deviceService.findByMacAddress("57:A7:D3:C2:50:2B");
        String actualTopology = deviceService.getDeviceTopology(device);
        assertEquals(expectedTopology, actualTopology);
    }

}
