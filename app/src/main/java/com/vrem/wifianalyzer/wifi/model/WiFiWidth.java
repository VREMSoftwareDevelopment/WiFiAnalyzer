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

enum WiFiWidth {
    MHZ_20(20),
    MHZ_40(40),
    MHZ_80(80),
    MHZ_160(160),
    MHZ_80_80(160);

    private final int frequencyWidth;
    private final int frequencyWidthHalf;
    private final int channelWidth;
    private final int channelWidthHalf;

    WiFiWidth(int frequencyWidth) {
        this.frequencyWidth = frequencyWidth;
        this.frequencyWidthHalf = frequencyWidth / 2;
        this.channelWidth = frequencyWidth / WiFiBand.CHANNEL_FREQUENCY_SPREAD;
        this.channelWidthHalf = channelWidth / 2;
    }

    int getFrequencyWidth() {
        return frequencyWidth;
    }

    int getFrequencyWidthHalf() {
        return frequencyWidthHalf;
    }

    int getChannelWidth() {
        return channelWidth;
    }

    int getChannelWidthHalf() {
        return channelWidthHalf;
    }
}
