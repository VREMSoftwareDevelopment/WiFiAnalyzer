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

package com.vrem.wifianalyzer.wifi.graph.channel;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphAdapter;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewNotifier;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ChannelGraphAdapter extends GraphAdapter {
    private final ChannelGraphNavigation channelGraphNavigation;

    protected ChannelGraphAdapter(@NonNull Scanner scanner, @NonNull Configuration configuration, @NonNull ChannelGraphNavigation channelGraphNavigation) {
        super(scanner, configuration);
        this.channelGraphNavigation = channelGraphNavigation;
    }

    @NonNull
    @Override
    public List<GraphViewNotifier> makeGraphViewNotifiers(@NonNull Configuration configuration) {
        Locale locale = configuration.getLocale();
        List<GraphViewNotifier> graphViewNotifiers = new ArrayList<>();
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            for (Pair<WiFiChannel, WiFiChannel> wiFiChannelPair : wiFiBand.getWiFiChannels().getWiFiChannelPairs(locale)) {
                graphViewNotifiers.add(new ChannelGraphView(wiFiBand, wiFiChannelPair));
            }
        }
        return graphViewNotifiers;
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        super.update(wiFiData);
        channelGraphNavigation.update();
    }
}
