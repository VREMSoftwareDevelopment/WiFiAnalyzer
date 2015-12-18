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

    private final ScanResult scanResult;

    private Details(@NonNull ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public static Details make(@NonNull ScanResult scanResult) {
        return new Details(scanResult);
    }

    public Frequency getFrequency() {
        return Frequency.find(scanResult.frequency);
    }

    public int getChannel() {
        return getFrequency().channel(scanResult.frequency);
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

}
