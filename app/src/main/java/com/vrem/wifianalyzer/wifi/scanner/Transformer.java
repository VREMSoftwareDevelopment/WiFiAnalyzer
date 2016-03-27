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

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.model.WiFiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transformer {
    static final String WI_FI_ANALYZER_BETA = "WiFi Analyzer BETA";

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

/*
    private void addTestData(@NonNull List<WiFiDetail> wiFiDetails) {
        if (isBeta()) {
            WiFiBand[] values = WiFiBand.values();
            int level = -50;
            int count = 0;
            for (WiFiBand wiFiBand : values) {
                WiFiWidth wiFiWidth = wiFiBand.isGHZ_5() ? WiFiWidth.MHZ_40 : WiFiWidth.MHZ_20;
                for (Pair<WiFiChannel, WiFiChannel> wiFiChannelPair : wiFiBand.getWiFiChannels().getWiFiChannelPairs()) {
                    wiFiDetails.addAll(Arrays.asList(
                            new WiFiDetail("SSID-T-" + count + "0", "BSSID:" + count + "0", Security.WPA.name(), new WiFiSignal(wiFiChannelPair.first.getFrequency(), wiFiWidth, level - 10)),
                            new WiFiDetail("SSID-T-" + count + "1", "BSSID:" + count + "1", Security.WEP.name(), new WiFiSignal(middle(wiFiChannelPair), wiFiWidth, level - 20)),
                            new WiFiDetail("SSID-T-" + count + "2", "BSSID:" + count + "2", Security.WPS.name(), new WiFiSignal(wiFiChannelPair.second.getFrequency(), wiFiWidth, level - 30))
                    ));
                    count++;
                }
            }
        }
    }

    private boolean isBeta() {
        return WI_FI_ANALYZER_BETA.equals(MainContext.INSTANCE.getContext().getString(R.string.app_name));
    }

    private int middle(@NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        return (wiFiChannelPair.first.getFrequency() + wiFiChannelPair.second.getFrequency()) / 2;
    }
*/

    private enum Fields {
        /*
                centerFreq0,
                centerFreq1,
        */
        channelWidth
    }

}
