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

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import com.vrem.util.BuildUtils;
import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.model.WiFiUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

class Transformer {

    @NonNull
    WiFiConnection transformWifiInfo(WifiInfo wifiInfo) {
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1) {
            return WiFiConnection.EMPTY;
        }
        return new WiFiConnection(
            WiFiUtils.convertSSID(wifiInfo.getSSID()),
            wifiInfo.getBSSID(),
            WiFiUtils.convertIpAddress(wifiInfo.getIpAddress()),
            wifiInfo.getLinkSpeed());
    }

    @NonNull
    List<WiFiDetail> transformCacheResults(List<CacheResult> cacheResults) {
        return new ArrayList<>(CollectionUtils.collect(cacheResults, new ToWiFiDetail()));
    }

    @NonNull
    WiFiWidth getWiFiWidth(@NonNull ScanResult scanResult) {
        try {
            return EnumUtils.find(WiFiWidth.class, getFieldValue(scanResult, Fields.channelWidth), WiFiWidth.MHZ_20);
        } catch (Exception e) {
            return WiFiWidth.MHZ_20;
        }
    }

    int getCenterFrequency(@NonNull ScanResult scanResult, @NonNull WiFiWidth wiFiWidth) {
        try {
            int centerFrequency = getFieldValue(scanResult, Fields.centerFreq0);
            if (centerFrequency == 0) {
                centerFrequency = scanResult.frequency;
            } else if (isExtensionFrequency(scanResult, wiFiWidth, centerFrequency)) {
                centerFrequency = (centerFrequency + scanResult.frequency) / 2;
            }
            return centerFrequency;
        } catch (Exception e) {
            return scanResult.frequency;
        }
    }

    boolean isExtensionFrequency(@NonNull ScanResult scanResult, @NonNull WiFiWidth wiFiWidth, int centerFrequency) {
        return WiFiWidth.MHZ_40.equals(wiFiWidth) && Math.abs(scanResult.frequency - centerFrequency) >= WiFiWidth.MHZ_40.getFrequencyWidthHalf();
    }

    int getFieldValue(@NonNull ScanResult scanResult, @NonNull Fields field) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = scanResult.getClass().getDeclaredField(field.name());
        return (int) declaredField.get(scanResult);
    }

    private boolean is80211mc(@NonNull ScanResult scanResult) {
        return BuildUtils.isMinVersionM() && scanResult.is80211mcResponder();
    }

    @NonNull
    WiFiData transformToWiFiData(List<CacheResult> cacheResults, WifiInfo wifiInfo) {
        List<WiFiDetail> wiFiDetails = transformCacheResults(cacheResults);
        WiFiConnection wiFiConnection = transformWifiInfo(wifiInfo);
        return new WiFiData(wiFiDetails, wiFiConnection);
    }

    enum Fields {
        centerFreq0,
        //        centerFreq1,
        channelWidth
    }

    private class ToWiFiDetail implements org.apache.commons.collections4.Transformer<CacheResult, WiFiDetail> {
        @Override
        public WiFiDetail transform(CacheResult input) {
            ScanResult scanResult = input.getScanResult();
            WiFiWidth wiFiWidth = getWiFiWidth(scanResult);
            int centerFrequency = getCenterFrequency(scanResult, wiFiWidth);
            WiFiSignal wiFiSignal = new WiFiSignal(scanResult.frequency, centerFrequency, wiFiWidth, input.getLevelAverage(), is80211mc(scanResult));
            return new WiFiDetail(scanResult.SSID, scanResult.BSSID, scanResult.capabilities, wiFiSignal);
        }
    }

}
