package com.device.api.services;

import com.device.api.model.Device;
import com.device.api.model.DeviceType;
import com.device.api.model.response.RegisterResponse;
import com.device.api.repository.DeviceRepository;
import com.device.api.repository.InMemoryDeviceRepository;
import com.device.api.util.DeviceTopologyUtil;
import com.device.api.util.MacAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAllDevices() {
        return deviceRepository.getDevices();
    }

    public Device findByMacAddress(String macAddress) {
        return deviceRepository.findByMacAddress(macAddress);
    }

    public boolean isDeviceRegistered(String macAddress) {
        return findByMacAddress(macAddress) != null;
    }

    public RegisterResponse registerDevice(DeviceType deviceType, String macAddress, String uplinkMacAddress) {
        List<String> errors = new ArrayList<>();

        if (!MacAddressUtil.isValidMacAddress(macAddress)) {
            errors.add(String.format("%s is not a valid MAC address!", macAddress));
        } else if (isDeviceRegistered(macAddress)) {
            errors.add(String.format("Device %s already registered!", macAddress));
        }

        Device uplinkDevice = null;
        if (uplinkMacAddress != null && !uplinkMacAddress.isEmpty()) {
            if (macAddress.equals(uplinkMacAddress)) {
                errors.add("Device MAC address cannot be the same as uplink device!");
            } else {
                uplinkDevice = findByMacAddress(uplinkMacAddress);
                if (uplinkDevice == null) {
                    errors.add(String.format("Uplink device with MAC address %s not found!", uplinkMacAddress));
                }
            }
        }

        if (errors.size() == 0) {
            Device newDevice = new Device(macAddress, deviceType, uplinkDevice);
            deviceRepository.addDevice(newDevice);
            return new RegisterResponse(true);
        }

        return new RegisterResponse(false, errors);
    }

    public String getFullTopology() {
        List<Device> rootDevices = deviceRepository.findRootDevices();
        if (rootDevices.size() == 0) return "No devices registered!";

        return DeviceTopologyUtil.getFullDeviceTopology(rootDevices);
    }

    public String getDeviceTopology(Device device) {
        return DeviceTopologyUtil.getDeviceTopology(device);
    }
}
