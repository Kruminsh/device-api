package com.device.api.repository;

import com.device.api.model.Device;
import java.util.List;

public interface DeviceRepository {
    List<Device> getDevices();
    List<Device> findRootDevices();
    Device findByMacAddress(String macAddress);
    void addDevice(Device device);
}

