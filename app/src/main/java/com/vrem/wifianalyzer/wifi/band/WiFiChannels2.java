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

package com.vrem.wifianalyzer.wifi.band;

import java.util.Arrays;

public class WiFiChannels2 extends WiFiChannels {
    private static final int FREQUENCY_SPREAD = 5;

    public WiFiChannels2() {
        super(Arrays.asList(
                new WiFiChannel(1, 2412),
                new WiFiChannel(2, 2417),
                new WiFiChannel(3, 2422),
                new WiFiChannel(4, 2427),
                new WiFiChannel(5, 2432),
                new WiFiChannel(6, 2437),
                new WiFiChannel(7, 2442),
                new WiFiChannel(8, 2447),
                new WiFiChannel(9, 2452),
                new WiFiChannel(10, 2457),
                new WiFiChannel(11, 2462),
                new WiFiChannel(12, 2467),
                new WiFiChannel(13, 2472),
                new WiFiChannel(14, 2484)));
    }

    @Override
    public WiFiChannel getWiFiChannelFirst() {
        return getChannels().get(0);
    }

    @Override
    public WiFiChannel getWiFiChannelLast() {
        return getChannels().get(getChannels().size() - 1);
    }

}
