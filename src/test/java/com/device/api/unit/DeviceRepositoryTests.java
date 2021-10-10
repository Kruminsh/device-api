package com.device.api.unit;

import com.device.api.model.Device;
import com.device.api.model.DeviceType;
import com.device.api.repository.DeviceRepository;
import com.device.api.repository.InMemoryDeviceRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceRepositoryTests {

    @Test
    public void canAddDevice() {
        DeviceRepository deviceRepository = new InMemoryDeviceRepository();

        Device device = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        deviceRepository.addDevice(device);

        assertEquals(1, deviceRepository.getDevices().size());
    }

    @Test
    public void canAddMultipleDevices() {
        DeviceRepository deviceRepository = new InMemoryDeviceRepository();

        Device device1 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        Device device2 = new Device("ca:22:16:2f:1b:0f", DeviceType.ACCESS_POINT, device1);
        Device device3 = new Device("f7:16:b6:68:22:0f", DeviceType.SWITCH, device2);

        deviceRepository.addDevice(device1);
        deviceRepository.addDevice(device2);
        deviceRepository.addDevice(device3);

        assertEquals(3, deviceRepository.getDevices().size());
    }

    @Test
    public void canFindRootDevices() {
        DeviceRepository deviceRepository = new InMemoryDeviceRepository();

        Device rootDevice1 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        Device childDevice1 = new Device("ca:22:16:2f:1b:0f", DeviceType.ACCESS_POINT, rootDevice1);

        Device rootDevice2 = new Device("ad-d6-dc-08-b9-7c", DeviceType.ACCESS_POINT);
        Device childDevice2 = new Device("ca:22:16:2f:1b:0f", DeviceType.ACCESS_POINT, rootDevice2);
        Device childDevice3 = new Device("ca:22:16:2f:1b:0f", DeviceType.ACCESS_POINT, childDevice2);

        deviceRepository.addDevice(rootDevice1);
        deviceRepository.addDevice(childDevice1);
        deviceRepository.addDevice(rootDevice2);
        deviceRepository.addDevice(childDevice2);
        deviceRepository.addDevice(childDevice3);

        List<Device> rootDevices = deviceRepository.findRootDevices();

        // Check if there's only 2 root devices in the repository
        assertEquals(2, rootDevices.size());

        // Check if root devices are correct
        assertTrue(rootDevices.contains(rootDevice1));
        assertTrue(rootDevices.contains(rootDevice2));
    }

    @Test
    public void canFindByExactMacAddress() {
        DeviceRepository deviceRepository = new InMemoryDeviceRepository();

        Device rootDevice1 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        Device childDevice1 = new Device("ca:22:16:2f:1b:0f", DeviceType.ACCESS_POINT, rootDevice1);
        Device rootDevice2 = new Device("ad-d6-dc-08-b9-7c", DeviceType.ACCESS_POINT);

        deviceRepository.addDevice(rootDevice1);
        deviceRepository.addDevice(childDevice1);
        deviceRepository.addDevice(rootDevice2);

        Device resultDevice = deviceRepository.findByMacAddress("ca:22:16:2f:1b:0f");
        assertNotNull(resultDevice);
        assertEquals(childDevice1, resultDevice);
    }

    @Test
    public void canFindByCaseInconsistentMacAddress() {
        DeviceRepository deviceRepository = new InMemoryDeviceRepository();

        Device rootDevice1 = new Device("7ded.b437.ed1d", DeviceType.GATEWAY);
        Device childDevice1 = new Device("ca:22:16:2f:1b:0f", DeviceType.ACCESS_POINT, rootDevice1);
        Device rootDevice2 = new Device("ad-d6-dc-08-b9-7c", DeviceType.ACCESS_POINT);

        deviceRepository.addDevice(rootDevice1);
        deviceRepository.addDevice(childDevice1);
        deviceRepository.addDevice(rootDevice2);

        Device resultDevice = deviceRepository.findByMacAddress("CA:22:16:2F:1B:0F");
        assertNotNull(resultDevice);
        assertEquals(childDevice1, resultDevice);

    }

    public void findByNonExistentMacAddressReturnsNull() {
        DeviceRepository deviceRepository = new InMemoryDeviceRepository();

        deviceRepository.addDevice(new Device("7ded.b437.ed1d", DeviceType.GATEWAY));

        Device searchResult = deviceRepository.findByMacAddress("ca:22:16:2f:1b:0f");
        assertNull(searchResult);
    }
}
