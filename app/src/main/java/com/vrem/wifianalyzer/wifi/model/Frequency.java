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

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

class Frequency implements WiFiFrequency {
    private final ScanResult scanResult;

    Frequency(@NonNull ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    public int getFrequency() {
        return scanResult.frequency;
    }

    @Override
    public int getFrequencyStart() {
        return getFrequency() - getWiFiWidth().getFrequencyWidthHalf();
    }

    @Override
    public int getFrequencyEnd() {
        return getFrequency() + getWiFiWidth().getFrequencyWidthHalf();
    }

    @Override
    public int getChannel() {
        return WiFiBand.findChannelByFrequency(getFrequency());
    }

    @Override
    public int getChannelStart() {
        return getChannel() - getWiFiWidth().getChannelWidthHalf();
    }

    @Override
    public int getChannelEnd() {
        return getChannel() + getWiFiWidth().getChannelWidthHalf();
    }

    @Override
    public WiFiBand getWiFiBand() {
        return WiFiBand.findByFrequency(getFrequency());
    }

    @Override
    public WiFiWidth getWiFiWidth() {
        return WiFiWidth.MHZ_20;
    }
}
