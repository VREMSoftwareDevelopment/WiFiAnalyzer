/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.model;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteOrder;

import androidx.annotation.NonNull;

public final class WiFiUtils {
    private static final double DISTANCE_MHZ_M = 27.55;
    private static final int MIN_RSSI = -100;
    private static final int MAX_RSSI = -55;
    private static final String QUOTE = "\"";

    private WiFiUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double calculateDistance(int frequency, int level) {
        return Math.pow(10.0, (DISTANCE_MHZ_M - (20 * Math.log10(frequency)) + Math.abs(level)) / 20.0);
    }

    public static int calculateSignalLevel(int rssi, int numLevels) {
        if (rssi <= MIN_RSSI) {
            return 0;
        }
        if (rssi >= MAX_RSSI) {
            return numLevels - 1;
        }
        return (rssi - MIN_RSSI) * (numLevels - 1) / (MAX_RSSI - MIN_RSSI);
    }

    @NonNull
    public static String convertSSID(@NonNull String ssid) {
        return StringUtils.removeEnd(StringUtils.removeStart(ssid, QUOTE), QUOTE);
    }

    @NonNull
    public static String convertIpAddress(int ipAddress) {
        try {
            byte[] ipBytes = BigInteger.valueOf(
                ByteOrder.LITTLE_ENDIAN.equals(ByteOrder.nativeOrder())
                    ? Integer.reverseBytes(ipAddress)
                    : ipAddress)
                .toByteArray();
            return InetAddress.getByAddress(ipBytes).getHostAddress();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

}
