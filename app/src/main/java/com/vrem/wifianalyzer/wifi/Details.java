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

public class Details implements DetailsInfo {
    private final ScanResult scanResult;

    public Details(@NonNull ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    public int getFrequency() {
        return scanResult.frequency;
    }

    @Override
    public int getChannel() {
        return Frequency.findChannel(scanResult.frequency);
    }

    @Override
    public Security getSecurity() {
        return Security.findOne(scanResult.capabilities);
    }

    @Override
    public Strength getStrength() {
        return Strength.calculate(scanResult.level);
    }

    @Override
    public String getSSID() {
        return scanResult.SSID;
    }

    @Override
    public String getBSSID() {
        return scanResult.BSSID;
    }

    @Override
    public int getLevel() {
        return Math.abs(scanResult.level);
    }

    @Override
    public String getCapabilities() {
        return scanResult.capabilities;
    }

    @Override
    public double getDistance() {
        return Distance.calculate(getFrequency(), getLevel());
    }
}
