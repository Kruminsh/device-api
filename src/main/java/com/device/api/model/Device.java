package com.device.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Device {
    private String macAddress;
    private DeviceType deviceType;
    @JsonIgnore
    private Device uplink;
    @JsonIgnore
    private final Set<Device> downlinkDevices;

    public Device(String macAddress, DeviceType deviceType) {
        this.macAddress = macAddress;
        this.deviceType = deviceType;
        this.downlinkDevices = new LinkedHashSet<>();
    }

    public Device(String macAddress, DeviceType deviceType, Device uplink) {
        this(macAddress, deviceType);
        this.uplink = uplink;
        if (uplink != null) uplink.addDownlinkDevice(this);
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public Device getUplink() {
        return this.uplink;
    }

    public Set<Device> getDownlinkDevices() {
        return this.downlinkDevices;
    }

    public void addDownlinkDevice(Device device) {
        this.downlinkDevices.add(device);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(macAddress, device.macAddress) && deviceType == device.deviceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(macAddress, deviceType);
    }
}
