package com.device.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MacAddressUtil {
    public static boolean isValidMacAddress(String macAddress) {
        // Regex to check a valid MAC address
        String regex = "^([0-9A-Fa-f]{2}[:-])"
                + "{5}([0-9A-Fa-f]{2})|"
                + "([0-9a-fA-F]{4}\\."
                + "[0-9a-fA-F]{4}\\."
                + "[0-9a-fA-F]{4})$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty return false
        if (macAddress == null) return false;

        // Find match between given string
        // and regular expression
        // using Pattern.matcher()
        Matcher m = p.matcher(macAddress);

        // Return if the string matched the ReGex
        return m.matches();
    }
}
