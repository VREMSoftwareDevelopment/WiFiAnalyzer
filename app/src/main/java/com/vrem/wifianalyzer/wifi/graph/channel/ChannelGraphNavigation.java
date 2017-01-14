/*
 * WiFi Analyzer
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphConstants;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import java.util.ArrayList;
import java.util.List;

class ChannelGraphNavigation implements GraphConstants {
    private final List<NavigationItem> navigationItems = new ArrayList<>();
    private final Configuration configuration;
    private final Context context;

    ChannelGraphNavigation(@NonNull Context context, @NonNull Configuration configuration) {
        this.context = context;
        this.configuration = configuration;
        makeNavigationItems();
    }

    List<NavigationItem> getNavigationItems() {
        return navigationItems;
    }

    void update() {
        List<NavigationItem> visible = getVisibleNavigationItems();

        Pair<WiFiChannel, WiFiChannel> selectedWiFiChannelPair = configuration.getWiFiChannelPair();
        for (NavigationItem navigationItem : navigationItems) {
            Button button = navigationItem.getButton();
            Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = navigationItem.getWiFiChannelPair();
            if (visible.size() > 1 && visible.contains(navigationItem)) {
                button.setVisibility(View.VISIBLE);
                setSelectedButton(button, wiFiChannelPair.equals(selectedWiFiChannelPair));
            } else {
                button.setVisibility(View.GONE);
                setSelectedButton(button, false);
            }
        }
    }

    private List<NavigationItem> getVisibleNavigationItems() {
        Settings settings = MainContext.INSTANCE.getSettings();
        WiFiBand wiFiBand = settings.getWiFiBand();
        String countryCode = settings.getCountryCode();
        WiFiChannels wiFiChannels = wiFiBand.getWiFiChannels();
        List<NavigationItem> visible = new ArrayList<>();
        for (NavigationItem navigationItem : navigationItems) {
            Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = navigationItem.getWiFiChannelPair();
            if (wiFiBand.isGHZ5() && wiFiChannels.isChannelAvailable(countryCode, wiFiChannelPair.first.getChannel())) {
                visible.add(navigationItem);
            }
        }
        return visible;
    }

    private void setSelectedButton(Button button, boolean selected) {
        if (selected) {
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.connected));
            button.setSelected(true);
        } else {
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.connected_background));
            button.setSelected(false);
        }
    }

    private void makeNavigationItems() {
        for (Pair<WiFiChannel, WiFiChannel> pair : WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairs()) {
            navigationItems.add(makeNavigationItem(pair));
        }
    }

    private NavigationItem makeNavigationItem(Pair<WiFiChannel, WiFiChannel> pair) {
        Button button = new Button(context);
        String text = pair.first.getChannel() + " - " + pair.second.getChannel();
        button.setLayoutParams(getLayoutParams());
        button.setVisibility(View.GONE);
        button.setText(text);
        button.setOnClickListener(new ButtonOnClickListener(pair));
        return new NavigationItem(button, pair);
    }

    @SuppressWarnings("ResourceType")
    @NonNull
    private LinearLayout.LayoutParams getLayoutParams() {
        int left = 5;
        int top = -30;
        if (configuration.isLargeScreen()) {
            left = 10;
            top = -10;
        }
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, TEXT_SIZE_ADJUSTMENT);
        params.setMargins(left, top, left, top);
        return params;
    }

    private class ButtonOnClickListener implements OnClickListener {
        private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

        ButtonOnClickListener(@NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
            this.wiFiChannelPair = wiFiChannelPair;
        }

        @Override
        public void onClick(View view) {
            configuration.setWiFiChannelPair(wiFiChannelPair);
            Scanner scanner = MainContext.INSTANCE.getScanner();
            scanner.update();
        }
    }

    class NavigationItem {
        private final Button button;
        private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

        NavigationItem(@NonNull Button button, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
            this.button = button;
            this.wiFiChannelPair = wiFiChannelPair;
        }

        Button getButton() {
            return button;
        }

        Pair<WiFiChannel, WiFiChannel> getWiFiChannelPair() {
            return wiFiChannelPair;
        }
    }

}
