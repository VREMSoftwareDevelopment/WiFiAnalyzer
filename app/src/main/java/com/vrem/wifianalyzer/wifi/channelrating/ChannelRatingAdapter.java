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

package com.vrem.wifianalyzer.wifi.channelrating;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vrem.util.BuildUtils;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.model.ChannelAPCount;
import com.vrem.wifianalyzer.wifi.model.ChannelRating;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.predicate.WiFiBandPredicate;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

class ChannelRatingAdapter extends ArrayAdapter<WiFiChannel> implements UpdateNotifier {
    private static final int MAX_CHANNELS_TO_DISPLAY = 10;

    private final TextView bestChannels;
    private ChannelRating channelRating;

    ChannelRatingAdapter(@NonNull Context context, @NonNull TextView bestChannels) {
        super(context, R.layout.channel_rating_details, new ArrayList<>());
        this.bestChannels = bestChannels;
        setChannelRating(new ChannelRating());
    }

    void setChannelRating(@NonNull ChannelRating channelRating) {
        this.channelRating = channelRating;
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        Settings settings = MainContext.INSTANCE.getSettings();
        WiFiBand wiFiBand = settings.getWiFiBand();
        List<WiFiChannel> wiFiChannels = setWiFiChannels(wiFiBand);
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(wiFiBand);
        List<WiFiDetail> wiFiDetails = wiFiData.getWiFiDetails(predicate, SortBy.STRENGTH);
        channelRating.setWiFiDetails(wiFiDetails);
        bestChannels(wiFiBand, wiFiChannels);
        notifyDataSetChanged();
    }

    @NonNull
    private List<WiFiChannel> setWiFiChannels(WiFiBand wiFiBand) {
        Settings settings = MainContext.INSTANCE.getSettings();
        String countryCode = settings.getCountryCode();
        List<WiFiChannel> wiFiChannels = wiFiBand.getWiFiChannels().getAvailableChannels(countryCode);
        clear();
        addAll(wiFiChannels);
        return wiFiChannels;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = MainContext.INSTANCE.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.channel_rating_details, parent, false);
        }

        WiFiChannel wiFiChannel = getItem(position);
        if (wiFiChannel == null) {
            return view;
        }

        view.<TextView>findViewById(R.id.channelNumber)
            .setText(String.format(Locale.ENGLISH, "%d", wiFiChannel.getChannel()));
        view.<TextView>findViewById(R.id.accessPointCount)
            .setText(String.format(Locale.ENGLISH, "%d", channelRating.getCount(wiFiChannel)));
        Strength strength = Strength.reverse(channelRating.getStrength(wiFiChannel));
        RatingBar ratingBar = view.findViewById(R.id.channelRating);
        int size = Strength.values().length;
        ratingBar.setMax(size);
        ratingBar.setNumStars(size);
        ratingBar.setRating(strength.ordinal() + 1);
        int color = ContextCompat.getColor(getContext(), strength.colorResource());
        setRatingBarColor(ratingBar, color);

        return view;
    }

    private void setRatingBarColor(RatingBar ratingBar, int color) {
        if (BuildUtils.isMinVersionL()) {
            ratingBar.setProgressTintList(ColorStateList.valueOf(color));
        } else {
            setRatingBarColorLegacy(ratingBar.getProgressDrawable(), color);
        }
    }

    @SuppressWarnings("deprecation")
    private void setRatingBarColorLegacy(Drawable drawable, int color) {
        try {
            int background = ContextCompat.getColor(getContext(), R.color.background);
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            layerDrawable.getDrawable(0).setColorFilter(background, PorterDuff.Mode.SRC_ATOP);
            layerDrawable.getDrawable(1).setColorFilter(background, PorterDuff.Mode.SRC_ATOP);
            layerDrawable.getDrawable(2).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        } catch (Exception e) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    void bestChannels(@NonNull WiFiBand wiFiBand, @NonNull List<WiFiChannel> wiFiChannels) {
        List<ChannelAPCount> channelAPCounts = channelRating.getBestChannels(wiFiChannels);
        int channelCount = 0;
        StringBuilder result = new StringBuilder();
        for (ChannelAPCount channelAPCount : channelAPCounts) {
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
            bestChannels.setTextColor(ContextCompat.getColor(getContext(), R.color.success));
        } else {
            Resources resources = getContext().getResources();
            StringBuilder message = new StringBuilder(resources.getText(R.string.channel_rating_best_none));
            if (WiFiBand.GHZ2.equals(wiFiBand)) {
                message.append(resources.getText(R.string.channel_rating_best_alternative));
                message.append(" ");
                message.append(getContext().getResources().getString(WiFiBand.GHZ5.getTextResource()));
            }
            bestChannels.setText(message);
            bestChannels.setTextColor(ContextCompat.getColor(getContext(), R.color.error));
        }
    }

}
