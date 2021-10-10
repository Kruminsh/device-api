package com.device.api.util;

import com.device.api.model.Device;

import java.util.Iterator;
import java.util.List;

public class DeviceTopologyUtil {
    private static final int INITIAL_STRING_BUILDER_CAP = 50;

    public static String getFullDeviceTopology(List<Device> devices) {
        String topologyString = "";
        for(Device device : devices) {
            topologyString += getDeviceTopology(device);
        }
        return topologyString;
    }

    public static String getDeviceTopology(Device device) {
        if (device == null) return "";
        StringBuilder buffer = new StringBuilder(INITIAL_STRING_BUILDER_CAP);
        printNode(device, buffer, "", "");
        return buffer.toString();
    }

    private static void printNode(Device device, StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(device.getMacAddress());
        buffer.append('\n');
        for (Iterator<Device> it = device.getDownlinkDevices().iterator(); it.hasNext();) {
            Device nextDevice = it.next();
            if (it.hasNext()) {
                printNode(nextDevice, buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                printNode(nextDevice, buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}
