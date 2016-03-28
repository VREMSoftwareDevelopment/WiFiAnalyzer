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
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphAdapter;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewNotifier;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import java.util.ArrayList;
import java.util.List;

class ChannelGraphAdapter extends GraphAdapter {
    private static final float TEXT_SIZE_ADJUSTMENT = 0.8f;

    private final List<Button> navigationItems = new ArrayList<>();

    ChannelGraphAdapter() {
        super();
        makeNavigationItems();
    }

    @NonNull
    @Override
    public List<GraphViewNotifier> makeGraphViewNotifiers() {
        List<GraphViewNotifier> graphViewNotifiers = new ArrayList<>();
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            for (Pair<WiFiChannel, WiFiChannel> wiFiChannelPair : wiFiBand.getWiFiChannels().getWiFiChannelPairs(MainContext.INSTANCE.getLocale())) {
                graphViewNotifiers.add(new ChannelGraphView(wiFiBand, wiFiChannelPair));
            }
        }
        return graphViewNotifiers;
    }

    List<Button> getNavigationItems() {
        return navigationItems;
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        super.update(wiFiData);
        WiFiBand wiFiBand = MainContext.INSTANCE.getSettings().getWiFiBand();
        for (Button button : navigationItems) {
            button.setVisibility(wiFiBand.isGHZ_5() ? View.VISIBLE : View.GONE);
        }
    }

    private void makeNavigationItems() {
        Context context = MainContext.INSTANCE.getContext();
        for (Pair<WiFiChannel, WiFiChannel> pair : WiFiBand.GHZ_5.getWiFiChannels().getWiFiChannelPairs(MainContext.INSTANCE.getLocale())) {
            navigationItems.add(makeNavigationItem(context, pair));
        }
    }

    private Button makeNavigationItem(@NonNull Context context, @NonNull Pair<WiFiChannel, WiFiChannel> pair) {
        Button button = new Button(context);
        String text = pair.first.getChannel() + " - " + pair.second.getChannel();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, TEXT_SIZE_ADJUSTMENT);
        params.setMargins(5, -30, 5, -30);
        button.setLayoutParams(params);
        button.setVisibility(View.GONE);
        button.setText(text);
        button.setOnClickListener(new ButtonOnClickListener(pair));
        setSelectedButton(button, pair.equals(MainContext.INSTANCE.getWiFiChannelPair()));
        return button;
    }

    private void setButtonsBackgroundColor(View view) {
        for (Button current : getNavigationItems()) {
            setSelectedButton(current, current.equals(view));
        }
    }

    private void setSelectedButton(Button button, boolean selected) {
        if (selected) {
            button.setBackgroundColor(MainContext.INSTANCE.getResources().getColor(R.color.connected));
            button.setSelected(true);
        } else {
            button.setBackgroundColor(MainContext.INSTANCE.getResources().getColor(R.color.connected_background));
            button.setSelected(false);
        }
    }

    class ButtonOnClickListener implements OnClickListener {
        private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;

        ButtonOnClickListener(@NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
            this.wiFiChannelPair = wiFiChannelPair;
        }

        @Override
        public void onClick(View view) {
            setButtonsBackgroundColor(view);
            MainContext.INSTANCE.setWiFiChannelPair(wiFiChannelPair);
            MainContext.INSTANCE.getScanner().update();
        }
    }

}
