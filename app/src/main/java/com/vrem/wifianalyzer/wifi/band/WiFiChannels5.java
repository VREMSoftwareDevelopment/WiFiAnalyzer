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

public class WiFiChannels5 extends WiFiChannels {
    public WiFiChannels5() {
        super(Arrays.asList(
                new WiFiChannel(183, 4915),     // 0
                new WiFiChannel(184, 4920),
                new WiFiChannel(185, 4925),
                new WiFiChannel(187, 4935),
                new WiFiChannel(188, 4940),
                new WiFiChannel(189, 4945),
                new WiFiChannel(192, 4960),
                new WiFiChannel(196, 4980),     // 7
                new WiFiChannel(7, 5035),       // 8
                new WiFiChannel(8, 5040),
                new WiFiChannel(9, 5045),
                new WiFiChannel(11, 5055),
                new WiFiChannel(12, 5060),
                new WiFiChannel(16, 5080),      // 9
                new WiFiChannel(34, 5170),      // 14
                new WiFiChannel(36, 5180),
                new WiFiChannel(38, 5190),
                new WiFiChannel(40, 5200),
                new WiFiChannel(42, 5210),
                new WiFiChannel(44, 5220),
                new WiFiChannel(46, 5230),
                new WiFiChannel(48, 5240),
                new WiFiChannel(52, 5260),
                new WiFiChannel(56, 5280),
                new WiFiChannel(60, 5300),
                new WiFiChannel(64, 5320),      // 25
                new WiFiChannel(100, 5500),     // 26
                new WiFiChannel(104, 5520),
                new WiFiChannel(108, 5540),
                new WiFiChannel(112, 5560),
                new WiFiChannel(116, 5580),
                new WiFiChannel(120, 5600),
                new WiFiChannel(124, 5620),
                new WiFiChannel(128, 5640),
                new WiFiChannel(132, 5660),
                new WiFiChannel(136, 5680),
                new WiFiChannel(140, 5700),     // 33
                new WiFiChannel(149, 5745),     // 34
                new WiFiChannel(153, 5765),
                new WiFiChannel(157, 5785),
                new WiFiChannel(161, 5805),
                new WiFiChannel(165, 5825))     // 38
        );
    }

    @Override
    public WiFiChannel getWiFiChannelFirst() {
        return getChannels().get(14);
    }

    @Override
    public WiFiChannel getWiFiChannelLast() {
        return getChannels().get(36);
    }

}
