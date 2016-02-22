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
import android.net.wifi.WifiInfo;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

class Connection {
    private static final String QUOTE = "\"";

    private final WifiInfo wifiInfo;

    Connection(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    String getIPAddress() {
        byte[] bytes = BigInteger.valueOf(wifiInfo.getIpAddress()).toByteArray();
        ArrayUtils.reverse(bytes);
        try {
            return InetAddress.getByAddress(bytes).getHostAddress();
        } catch (UnknownHostException e) {
            return StringUtils.EMPTY;
        }
    }

    private String getSSID() {
        return StringUtils.removeEnd(StringUtils.removeStart(wifiInfo.getSSID(), QUOTE), QUOTE);
    }

    public boolean matches(ScanResult scanResult) {
        return wifiInfo != null
                && scanResult != null
                && wifiInfo.getNetworkId() != -1
                && getSSID().equalsIgnoreCase(scanResult.SSID)
                && wifiInfo.getBSSID().equalsIgnoreCase(scanResult.BSSID);
    }
}
