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

package com.vrem.wifianalyzer.wifi;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.ChannelRating;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import java.util.ArrayList;
import java.util.SortedSet;

class ChannelRatingAdapter extends ArrayAdapter<Integer> implements UpdateNotifier {
    public static final int MAX_CHANNELS_TO_DISPLAY = 10;
    private final MainContext mainContext = MainContext.INSTANCE;
    private final Resources resources;
    private final ChannelRating channelRating;
    private final TextView bestChannels;

    ChannelRatingAdapter(@NonNull Context context, @NonNull TextView bestChannels) {
        super(context, R.layout.channel_rating_details, new ArrayList<Integer>());
        this.resources = context.getResources();
        this.bestChannels = bestChannels;
        mainContext.getScanner().addUpdateNotifier(this);
        this.channelRating = new ChannelRating();
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        WiFiBand wiFiBand = mainContext.getSettings().getWiFiBand();
        clear();
        SortedSet<Integer> channels = wiFiBand.getChannels();
        addAll(channels);
        channelRating.setWiFiChannels(wiFiData.getWiFiChannels(wiFiBand));
        bestChannels(channels);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mainContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.channel_rating_details, parent, false);
        }

        Integer channel = getItem(position);
        int count = channelRating.getCount(channel);

        ((TextView) convertView.findViewById(R.id.channelNumber)).setText(String.format("%d", channel));
        ((TextView) convertView.findViewById(R.id.accessPointCount)).setText(String.format("%d", count));

        Strength strength = Strength.reverse(channelRating.getStrength(channel));
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.channelRating);
        int size = Strength.values().length;
        ratingBar.setMax(size);
        ratingBar.setNumStars(size);
        ratingBar.setRating(strength.ordinal() + 1);
        ratingBar.setProgressTintList(ColorStateList.valueOf(resources.getColor(strength.colorResource())));

        return convertView;
    }

    void bestChannels(SortedSet<Integer> channels) {
        int channelCount = 0;
        StringBuilder result = new StringBuilder();
        for (Integer channel : channels) {
            if (channelCount > MAX_CHANNELS_TO_DISPLAY) {
                result.append("...");
                break;
            }
            Strength strength = Strength.reverse(channelRating.getStrength(channel));
            if (Strength.FOUR.equals(strength) || Strength.THREE.equals(strength)) {
                if (result.length() > 0) {
                    result.append(", ");
                }
                result.append(channel);
                channelCount++;
            }
        }
        if (result.length() > 0) {
            bestChannels.setText(result.toString());
            bestChannels.setTextColor(resources.getColor(R.color.success_color));
        } else {
            bestChannels.setText(resources.getText(R.string.channel_rating_best_none));
            bestChannels.setTextColor(resources.getColor(R.color.error_color));
        }
    }

}
