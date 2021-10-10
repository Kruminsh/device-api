package com.device.api.repository;

import com.device.api.model.Device;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryDeviceRepository implements DeviceRepository {
    private final String MAC_REPLACEABLE_CHARS = "[-.:]";
    private final String MAC_CHARS_REPLACEMENT = "";
    private final List<Device> devices = new LinkedList<>();

    @Override
    public List<Device> getDevices() {
        return devices.stream()
                .sorted(Comparator.comparingInt(device -> device.getDeviceType().ordinal()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Device> findRootDevices() {
        return devices.stream()
                .filter(device -> device.getUplink() == null)
                .collect(Collectors.toList());
    }

    @Override
    public Device findByMacAddress(String macAddress) {
        return devices.stream()
                .filter(device ->
                        device.getMacAddress().replaceAll(MAC_REPLACEABLE_CHARS, MAC_CHARS_REPLACEMENT)
                        .equalsIgnoreCase(macAddress.replaceAll(MAC_REPLACEABLE_CHARS, MAC_CHARS_REPLACEMENT)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addDevice(Device device) {
        devices.add(device);
    }
}
