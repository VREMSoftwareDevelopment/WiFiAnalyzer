/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.graph.channel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;

import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelsGHZ5;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphConstants;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class ChannelGraphNavigation implements GraphConstants {
    static final Map<Pair<WiFiChannel, WiFiChannel>, Integer> ids = new HashMap<>();

    static {
        ids.put(WiFiChannelsGHZ5.SET1, R.id.graphNavigationSet1);
        ids.put(WiFiChannelsGHZ5.SET2, R.id.graphNavigationSet2);
        ids.put(WiFiChannelsGHZ5.SET3, R.id.graphNavigationSet3);
    }

    private final Context context;
    private final View view;

    ChannelGraphNavigation(@NonNull View view, @NonNull Context context) {
        this.view = view;
        this.context = context;
        for (Pair<WiFiChannel, WiFiChannel> pair : ids.keySet()) {
            view.findViewById(ids.get(pair)).setOnClickListener(new SetOnClickListener(pair));
        }
    }

    void update() {
        Collection<Pair<WiFiChannel, WiFiChannel>> visible = CollectionUtils.select(ids.keySet(), new PairPredicate());
        int size = visible.size();
        if (size > 1) {
            Configuration configuration = MainContext.INSTANCE.getConfiguration();
            Pair<WiFiChannel, WiFiChannel> selectedWiFiChannelPair = configuration.getWiFiChannelPair();
            for (Pair<WiFiChannel, WiFiChannel> pair : ids.keySet()) {
                View view = this.view.findViewById(ids.get(pair));
                if (size > 1 && visible.contains(pair)) {
                    view.setVisibility(View.VISIBLE);
                    setSelectedButton(view, pair.equals(selectedWiFiChannelPair));
                } else {
                    view.setVisibility(View.GONE);
                    setSelectedButton(view, false);
                }
            }
            this.view.setVisibility(View.VISIBLE);
        } else {
            this.view.setVisibility(View.GONE);
        }
    }

    private void setSelectedButton(View view, boolean selected) {
        if (selected) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.connected));
            view.setSelected(true);
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.connected_background));
            view.setSelected(false);
        }
    }

    static class SetOnClickListener implements OnClickListener {
        private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

        SetOnClickListener(@NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
            this.wiFiChannelPair = wiFiChannelPair;
        }

        @Override
        public void onClick(View view) {
            MainContext mainContext = MainContext.INSTANCE;
            mainContext.getConfiguration().setWiFiChannelPair(wiFiChannelPair);
            Scanner scanner = mainContext.getScanner();
            scanner.update();
        }
    }

    private class PairPredicate implements Predicate<Pair<WiFiChannel, WiFiChannel>> {
        private final WiFiBand wiFiBand;
        private final String countryCode;
        private final WiFiChannels wiFiChannels;

        private PairPredicate() {
            Settings settings = MainContext.INSTANCE.getSettings();
            wiFiBand = settings.getWiFiBand();
            countryCode = settings.getCountryCode();
            wiFiChannels = wiFiBand.getWiFiChannels();
        }

        @Override
        public boolean evaluate(Pair<WiFiChannel, WiFiChannel> object) {
            return wiFiBand.isGHZ5() && wiFiChannels.isChannelAvailable(countryCode, object.first.getChannel());
        }
    }

}
