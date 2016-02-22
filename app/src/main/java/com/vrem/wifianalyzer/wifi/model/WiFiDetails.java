/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.model;

import java.util.List;

public interface WiFiDetails {
    String getTitle();

    String getSSID();

    String getBSSID();

    int getFrequency();

    int getChannel();

    WiFiBand getWiFiBand();

    Security getSecurity();

    Strength getStrength();

    int getLevel();

    String getCapabilities();

    double getDistance();

    String getVendorName();

    String getIPAddress();

    boolean isConnected();

    boolean isConfiguredNetwork();

    List<WiFiDetails> getChildren();

    void addChild(WiFiDetails wiFiDetails);
}
