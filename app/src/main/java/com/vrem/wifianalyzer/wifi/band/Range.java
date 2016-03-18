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

class Range {
    private final int frequencyStart;
    private final int frequencyEnd;
    private final int channelFirst;

    Range(int frequencyStart, int frequencyEnd, int channelFirst) {
        this.frequencyStart = frequencyStart;
        this.frequencyEnd = frequencyEnd;
        this.channelFirst = channelFirst;
    }

    int getChannelByFrequency(int value) {
        if (value >= frequencyStart && value <= frequencyEnd) {
            return (value - frequencyStart) / WiFiRange.CHANNEL_FREQUENCY_SPREAD + channelFirst;
        }
        return 0;
    }

    int getFrequencyStart() {
        return frequencyStart;
    }

    int getFrequencyEnd() {
        return frequencyEnd;
    }

    int getChannelFirst() {
        return channelFirst;
    }

    int getChannelLast() {
        return getChannelByFrequency(getFrequencyEnd());
    }
}
