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

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
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
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import java.util.ArrayList;
import java.util.List;

class ChannelGraphNavigation {
    private static final float TEXT_SIZE_ADJUSTMENT = 0.8f;
    private final List<Button> navigationItems = new ArrayList<>();
    private final Settings settings;
    private final Configuration configuration;
    private Resources resources;
    private Scanner scanner;

    ChannelGraphNavigation(@NonNull Context context, @NonNull Settings settings, @NonNull Configuration configuration) {
        this.settings = settings;
        this.configuration = configuration;
        makeNavigationItems(context);
    }

    protected List<Button> getNavigationItems() {
        return navigationItems;
    }

    protected void update() {
        WiFiBand wiFiBand = settings.getWiFiBand();
        for (Button button : navigationItems) {
            button.setVisibility(wiFiBand.isGHZ5() ? View.VISIBLE : View.GONE);
        }
    }

    private void makeNavigationItems(@NonNull Context context) {
        String countryCode = settings.getCountryCode();
        List<Pair<WiFiChannel, WiFiChannel>> wiFiChannelPairs = WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairs(countryCode);
        if (wiFiChannelPairs.size() > 1) {
            Pair<WiFiChannel, WiFiChannel> selected = configuration.getWiFiChannelPair();
            for (Pair<WiFiChannel, WiFiChannel> pair : wiFiChannelPairs) {
                navigationItems.add(makeNavigationItem(context, configuration, pair, pair.equals(selected)));
            }
        }
    }

    private Button makeNavigationItem(@NonNull Context context, @NonNull Configuration configuration, Pair<WiFiChannel, WiFiChannel> pair, boolean selected) {
        Button button = new Button(context);
        String text = pair.first.getChannel() + " - " + pair.second.getChannel();
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, TEXT_SIZE_ADJUSTMENT);
        if (configuration.isLargeScreenLayout()) {
            params.setMargins(10, -10, 10, -10);
        } else {
            params.setMargins(5, -30, 5, -30);
        }
        button.setLayoutParams(params);
        button.setVisibility(View.GONE);
        button.setText(text);
        button.setOnClickListener(new ButtonOnClickListener(pair));
        setSelectedButton(button, selected);
        return button;
    }

    private void setButtonsBackgroundColor(View view) {
        for (Button current : getNavigationItems()) {
            setSelectedButton(current, current.equals(view));
        }
    }

    private void setSelectedButton(Button button, boolean selected) {
        if (selected) {
            button.setBackgroundColor(getResources().getColor(R.color.connected));
            button.setSelected(true);
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.connected_background));
            button.setSelected(false);
        }
    }

    private class ButtonOnClickListener implements OnClickListener {
        private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

        ButtonOnClickListener(@NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
            this.wiFiChannelPair = wiFiChannelPair;
        }

        @Override
        public void onClick(View view) {
            setButtonsBackgroundColor(view);
            configuration.setWiFiChannelPair(wiFiChannelPair);
            getScanner().update();
        }
    }

    // injectors start
    private Resources getResources() {
        if (resources == null) {
            resources = MainContext.INSTANCE.getResources();
        }
        return resources;
    }

    protected void setResources(@NonNull Resources resources) {
        this.resources = resources;
    }

    private Scanner getScanner() {
        if (scanner == null) {
            scanner = MainContext.INSTANCE.getScanner();
        }
        return scanner;
    }

    protected void setScanner(@NonNull Scanner scanner) {
        this.scanner = scanner;
    }
    // injectors end
}
