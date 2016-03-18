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

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.model.WiFiUtils;
import com.vrem.wifianalyzer.wifi.model.WiFiWidth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transformer {
    WiFiConnection transformWifiInfo(WifiInfo wifiInfo) {
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1) {
            return WiFiConnection.EMPTY;
        }
        return new WiFiConnection(WiFiUtils.convertSSID(wifiInfo.getSSID()), wifiInfo.getBSSID(), WiFiUtils.convertIpAddress(wifiInfo.getIpAddress()));
    }

    List<String> transformWifiConfigurations(List<WifiConfiguration> configuredNetworks) {
        List<String> results = new ArrayList<>();
        if (configuredNetworks != null) {
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                results.add(WiFiUtils.convertSSID(wifiConfiguration.SSID));
            }
        }
        return Collections.unmodifiableList(results);
    }

    List<WiFiDetail> transformScanResults(List<ScanResult> scanResults) {
        List<WiFiDetail> results = new ArrayList<>();
        if (scanResults != null) {
            for (ScanResult scanResult : scanResults) {
                WiFiSignal wiFiSignal = new WiFiSignal(scanResult.frequency, getWiFiWidth(scanResult), scanResult.level);
                WiFiDetail wiFiDetail = new WiFiDetail(scanResult.SSID, scanResult.BSSID, scanResult.capabilities, wiFiSignal);
                results.add(wiFiDetail);
            }
        }
/*
        addTestData(results);
*/
        return Collections.unmodifiableList(results);
    }

    private WiFiWidth getWiFiWidth(ScanResult scanResult) {
        try {
            return WiFiWidth.find((int) scanResult.getClass().getDeclaredField(Fields.channelWidth.name()).get(scanResult));
        } catch (Exception e) {
            // Not APK 23+ can not convert
            return WiFiWidth.MHZ_20;
        }
    }

    public WiFiData transformToWiFiData(List<ScanResult> scanResults, WifiInfo wifiInfo, List<WifiConfiguration> configuredNetworks) {
        List<WiFiDetail> wiFiDetails = transformScanResults(scanResults);
        WiFiConnection wiFiConnection = transformWifiInfo(wifiInfo);
        List<String> wifiConfigurations = transformWifiConfigurations(configuredNetworks);
        return new WiFiData(wiFiDetails, wiFiConnection, wifiConfigurations);
    }

    private enum Fields {
        channelWidth,
        centerFreq0,
        centerFreq1
    }

/*
    private void addTestData(List<WiFiDetail> wiFiDetails) {
        int level = -60;
        wiFiDetails.addAll(Arrays.asList(
                new WiFiDetail("SSID-TEST1", "BSSID:2.4GHZ:01", Security.WPA.name(), new WiFiSignal(WiFiBand.GHZ_2.getFrequencyStart(), WiFiWidth.MHZ_20, level)),
                new WiFiDetail("SSID-TEST2", "BSSID:2.4GHZ:02", Security.WEP.name(), new WiFiSignal(WiFiBand.GHZ_2.getFrequencyEnd(), WiFiWidth.MHZ_20, level)),
                new WiFiDetail("SSID-TEST3", "BSSID:5.0GHZ:03", Security.WPA.name(), new WiFiSignal(WiFiBand.GHZ_5.getFrequencyStart(), WiFiWidth.MHZ_20, level)),
                new WiFiDetail("SSID-TEST4", "BSSID:5.0GHZ:04", Security.WEP.name(), new WiFiSignal(WiFiBand.GHZ_5.getFrequencyEnd(), WiFiWidth.MHZ_20, level))));
    }
*/
}
