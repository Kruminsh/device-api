package com.device.api.services;

import com.device.api.model.Device;
import com.device.api.model.DeviceType;
import com.device.api.model.response.RegisterResponse;

import java.util.List;

public interface DeviceService {
    List<Device> getAllDevices();
    Device findByMacAddress(String macAddress);
    boolean isDeviceRegistered(String macAddress);
    RegisterResponse registerDevice(DeviceType deviceType, String macAddress, String uplinkMacAddress);
    String getFullTopology();
    String getDeviceTopology(Device device);
}
