/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.wifi;

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

public class Details {

    public static final double DISTANCE_MHZ_M = 27.55;

    private final ScanResult scanResult;
    private final String ipAddress;

    private Details(@NonNull ScanResult scanResult, @NonNull String ipAddress) {
        this.scanResult = scanResult;
        this.ipAddress = ipAddress;
    }

    public static Details make(@NonNull ScanResult scanResult, @NonNull String ipAddress) {
        return new Details(scanResult, ipAddress);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getFrequency() {
        return scanResult.frequency;
    }

    public int getChannel() {
        return Frequency.findChannel(scanResult.frequency);
    }

    public Security getSecurity() {
        return Security.findOne(scanResult.capabilities);
    }

    public Strength getStrength() {
        return Strength.calculate(scanResult.level);
    }

    public String getSSID() {
        return scanResult.SSID;
    }

    public String getBSSID() {
        return scanResult.BSSID;
    }

    public int getLevel() {
        return scanResult.level;
    }

    public String getCapabilities() {
        return scanResult.capabilities;
    }

    public double getDistance()    {
        return Math.pow(10.0,
            (DISTANCE_MHZ_M - (20 * Math.log10(scanResult.frequency)) + Math.abs(scanResult.level)) / 20.0);
    }

}
