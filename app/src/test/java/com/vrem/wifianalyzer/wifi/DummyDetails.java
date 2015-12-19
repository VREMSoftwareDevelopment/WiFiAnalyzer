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

import org.mockito.Mockito;

public class DummyDetails extends Details {
    private final String ssid;
    private final String bssid;
    private final int level;

    public DummyDetails(@NonNull ScanResult scanResult, @NonNull String ssid, @NonNull String bssid, int level) {
        super(scanResult);
        this.ssid = ssid;
        this.bssid = bssid;
        this.level = level;
    }

    @Override
    public String getSSID() {
        return ssid;
    }

    @Override
    public String getBSSID() {
        return bssid;
    }

    @Override
    public int getLevel() {
        return level;
    }

}
