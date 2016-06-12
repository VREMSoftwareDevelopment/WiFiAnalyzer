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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.model.ChannelRating;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import java.util.ArrayList;
import java.util.List;

class ChannelRatingAdapter extends ArrayAdapter<WiFiChannel> implements UpdateNotifier {
    private static final int MAX_CHANNELS_TO_DISPLAY = 10;

    private final Resources resources;
    private final TextView bestChannels;
    private ChannelRating channelRating;

    public ChannelRatingAdapter(@NonNull Context context, @NonNull TextView bestChannels) {
        super(context, R.layout.channel_rating_details, new ArrayList<WiFiChannel>());
        this.resources = context.getResources();
        this.bestChannels = bestChannels;
        setChannelRating(new ChannelRating());
        MainContext.INSTANCE.getScanner().addUpdateNotifier(this);
    }

    protected void setChannelRating(@NonNull ChannelRating channelRating) {
        this.channelRating = channelRating;
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        WiFiBand wiFiBand = MainContext.INSTANCE.getSettings().getWiFiBand();
        List<WiFiChannel> wiFiChannels = setWiFiChannels(wiFiBand);
        channelRating.setWiFiDetails(wiFiData.getWiFiDetails(wiFiBand, SortBy.STRENGTH));
        bestChannels(wiFiBand, wiFiChannels);
        notifyDataSetChanged();
    }

    private List<WiFiChannel> setWiFiChannels(WiFiBand wiFiBand) {
        String countryCode = MainContext.INSTANCE.getSettings().getCountryCode();
        List<WiFiChannel> wiFiChannels = wiFiBand.getWiFiChannels().getAvailableChannels(countryCode);
        clear();
        addAll(wiFiChannels);
        return wiFiChannels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = MainContext.INSTANCE.getLayoutInflater().inflate(R.layout.channel_rating_details, parent, false);
        }

        WiFiChannel wiFiChannel = getItem(position);
        int count = channelRating.getCount(wiFiChannel);

        ((TextView) view.findViewById(R.id.channelNumber)).setText(String.format("%d", wiFiChannel.getChannel()));
        ((TextView) view.findViewById(R.id.accessPointCount)).setText(String.format("%d", count));

        Strength strength = Strength.reverse(channelRating.getStrength(wiFiChannel));
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.channelRating);
        int size = Strength.values().length;
        ratingBar.setMax(size);
        ratingBar.setNumStars(size);
        ratingBar.setRating(strength.ordinal() + 1);
        ratingBar.setProgressTintList(ColorStateList.valueOf(resources.getColor(strength.colorResource())));

        return view;
    }

    protected void bestChannels(@NonNull WiFiBand wiFiBand, @NonNull List<WiFiChannel> wiFiChannels) {
        List<ChannelRating.ChannelAPCount> channelAPCounts = channelRating.getBestChannels(wiFiChannels);
        int channelCount = 0;
        StringBuilder result = new StringBuilder();
        for (ChannelRating.ChannelAPCount channelAPCount : channelAPCounts) {
            if (channelCount > MAX_CHANNELS_TO_DISPLAY) {
                result.append("...");
                break;
            }
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(channelAPCount.getWiFiChannel().getChannel());
            channelCount++;
        }
        if (result.length() > 0) {
            bestChannels.setText(result.toString());
            bestChannels.setTextColor(resources.getColor(R.color.success_color));
        } else {
            StringBuilder message = new StringBuilder(resources.getText(R.string.channel_rating_best_none));
            if (WiFiBand.GHZ2.equals(wiFiBand)) {
                message.append(resources.getText(R.string.channel_rating_best_alternative));
                message.append(" ");
                message.append(WiFiBand.GHZ5.getBand());
            }
            bestChannels.setText(message);
            bestChannels.setTextColor(resources.getColor(R.color.error_color));
        }
    }

}
