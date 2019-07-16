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

package com.vrem.wifianalyzer.wifi.channelgraph;

import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.apache.commons.collections4.Predicate;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

class InRangePredicate implements Predicate<WiFiDetail> {
    private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

    InRangePredicate(@NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        this.wiFiChannelPair = wiFiChannelPair;
    }

    @Override
    public boolean evaluate(WiFiDetail object) {
        int frequency = object.getWiFiSignal().getCenterFrequency();
        return frequency >= wiFiChannelPair.first.getFrequency() && frequency <= wiFiChannelPair.second.getFrequency();
    }
}
