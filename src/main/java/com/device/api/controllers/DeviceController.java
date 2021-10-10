package com.device.api.controllers;

import com.device.api.exception.ApiRequestException;
import com.device.api.model.Device;
import com.device.api.model.DeviceType;
import com.device.api.model.response.RegisterResponse;
import com.device.api.services.DeviceService;
import com.device.api.services.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("device")
public class DeviceController {

    @Autowired
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public List<Device> getAll() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/findByMacAddress")
    public Device findByMacAddress(@RequestParam String macAddress) {
        Device device = deviceService.findByMacAddress(macAddress);
        if (device == null) throw new ApiRequestException(String.format("Device '%s' not found!", macAddress), HttpStatus.NOT_FOUND);
        return deviceService.findByMacAddress(macAddress);
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestParam DeviceType deviceType, @RequestParam String macAddress,
                                     @RequestParam(required = false) String uplinkMacAddress) {
        return deviceService.registerDevice(deviceType, macAddress, uplinkMacAddress);
    }

    @GetMapping("/topology")
    public String deviceTopology(@RequestParam(required = false) String macAddress) {
        if (macAddress != null) {
            Device device = deviceService.findByMacAddress(macAddress);
            if (device == null) throw new ApiRequestException(String.format("Device '%s' not found!", macAddress), HttpStatus.NOT_FOUND);
            return deviceService.getDeviceTopology(device);
        }
        return deviceService.getFullTopology();
    }

}
