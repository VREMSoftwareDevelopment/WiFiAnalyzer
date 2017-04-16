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

package com.vrem.wifianalyzer.wifi.channelgraph;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vrem.util.TextUtils;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelsGHZ5;
import com.vrem.wifianalyzer.wifi.graphutils.GraphConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.predicate.FilterPredicate;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class ChannelGraphNavigation implements GraphConstants {
    static final Map<Pair<WiFiChannel, WiFiChannel>, Integer> ids = new HashMap<>();
    private static final String ACTIVITY_NONE = "&#8722";
    private static final String ACTIVITY_ON = "&#9585;&#9586;";

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

    void update(@NonNull WiFiData wiFiData) {
        Collection<Pair<WiFiChannel, WiFiChannel>> visible = CollectionUtils.select(ids.keySet(), new PairPredicate());
        updateButtons(wiFiData, visible);
        view.setVisibility(visible.size() > 1 ? View.VISIBLE : View.GONE);
    }

    private void updateButtons(@NonNull WiFiData wiFiData, Collection<Pair<WiFiChannel, WiFiChannel>> visible) {
        if (visible.size() > 1) {
            MainContext mainContext = MainContext.INSTANCE;
            Configuration configuration = mainContext.getConfiguration();
            Pair<WiFiChannel, WiFiChannel> selectedWiFiChannelPair = configuration.getWiFiChannelPair();

            Settings settings = mainContext.getSettings();
            Predicate<WiFiDetail> predicate = FilterPredicate.makeOtherPredicate(settings);
            List<WiFiDetail> wiFiDetails = wiFiData.getWiFiDetails(predicate, settings.getSortBy());
            for (Pair<WiFiChannel, WiFiChannel> pair : ids.keySet()) {
                Button button = (Button) view.findViewById(ids.get(pair));
                if (visible.contains(pair)) {
                    button.setVisibility(View.VISIBLE);
                    setSelected(button, pair.equals(selectedWiFiChannelPair));
                    setActivity(button, pair, IterableUtils.matchesAny(wiFiDetails, new InRangePredicate(pair)));
                } else {
                    button.setVisibility(View.GONE);
                    setSelected(button, false);
                }
            }
        }
    }

    private void setSelected(Button button, boolean selected) {
        if (selected) {
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.connected));
            button.setSelected(true);
        } else {
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.connected_background));
            button.setSelected(false);
        }
    }

    private void setActivity(Button button, Pair<WiFiChannel, WiFiChannel> pair, boolean activity) {
        button.setText(TextUtils.fromHtml(String.format(Locale.ENGLISH, "<strong>%d %s %d</strong>",
            pair.first.getChannel(),
            activity ? ACTIVITY_ON : ACTIVITY_NONE,
            pair.second.getChannel())));
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
