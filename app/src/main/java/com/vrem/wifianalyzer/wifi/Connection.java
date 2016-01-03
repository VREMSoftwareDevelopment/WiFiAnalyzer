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
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

class Connection {
    private final WifiInfo wifiInfo;

    Connection(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    String getIPAddress(ScanResult scanResult) {
        if (match(scanResult)) {
            byte[] bytes = BigInteger.valueOf(wifiInfo.getIpAddress()).toByteArray();
            ArrayUtils.reverse(bytes);
            try {
                return InetAddress.getByAddress(bytes).getHostAddress();
            } catch (UnknownHostException e) {
                // invalid ip address
            }
        }
        return StringUtils.EMPTY;
    }

    private String getSSID() {
        String result = wifiInfo.getSSID();
        if (result.charAt(0) == '"') {
            result = result.substring(1);
        }
        if (result.charAt(result.length() - 1) == '"') {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private boolean match(@NonNull ScanResult scanResult) {
        return wifiInfo != null
            && scanResult != null
            && wifiInfo.getNetworkId() != -1
            && getSSID().equals(scanResult.SSID)
            && wifiInfo.getBSSID().equals(scanResult.BSSID);
    }
}
