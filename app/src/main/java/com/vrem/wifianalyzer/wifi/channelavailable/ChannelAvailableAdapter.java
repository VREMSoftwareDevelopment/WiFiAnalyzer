/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.channelavailable;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.databinding.ChannelAvailableDetailsBinding;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class ChannelAvailableAdapter extends ArrayAdapter<WiFiChannelCountry> {
    private static final String SEPARATOR = ",";

    ChannelAvailableAdapter(@NonNull Context context, @NonNull List<WiFiChannelCountry> wiFiChannelCountries) {
        super(context, R.layout.channel_available_details, wiFiChannelCountries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Binding binding = view == null ? new Binding(create(parent)) : new Binding(view);
        View rootView = binding.getRoot();
        Resources resources = rootView.getResources();

        Locale currentLocale = MainContext.INSTANCE.getSettings().languageLocale();
        WiFiChannelCountry wiFiChannelCountry = getItem(position);
        binding.getChannelAvailableCountry()
            .setText(wiFiChannelCountry.countryCode() + " - " + wiFiChannelCountry.countryName(currentLocale));
        binding.getChannelAvailableTitleGhz2()
            .setText(String.format(Locale.ENGLISH, "%s : ",
                resources.getString(WiFiBand.GHZ2.getTextResource())));
        binding.getChannelAvailableGhz2()
            .setText(TextUtils.join(SEPARATOR, wiFiChannelCountry.channelsGHZ2().toArray()));
        binding.getChannelAvailableTitleGhz5()
            .setText(String.format(Locale.ENGLISH, "%s : ",
                resources.getString(WiFiBand.GHZ5.getTextResource())));
        binding.getChannelAvailableGhz5()
            .setText(TextUtils.join(SEPARATOR, wiFiChannelCountry.channelsGHZ5().toArray()));
        return rootView;
    }

    private ChannelAvailableDetailsBinding create(@NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = MainContext.INSTANCE.getLayoutInflater();
        return ChannelAvailableDetailsBinding.inflate(layoutInflater, parent, false);
    }


    private class Binding {
        private final View root;
        private final TextView channelAvailableCountry;
        private final TextView channelAvailableTitleGhz2;
        private final TextView channelAvailableGhz2;
        private final TextView channelAvailableTitleGhz5;
        private final TextView channelAvailableGhz5;

        Binding(@NonNull ChannelAvailableDetailsBinding binding) {
            root = binding.getRoot();
            channelAvailableCountry = binding.channelAvailableCountry;
            channelAvailableTitleGhz2 = binding.channelAvailableTitleGhz2;
            channelAvailableGhz2 = binding.channelAvailableGhz2;
            channelAvailableTitleGhz5 = binding.channelAvailableTitleGhz5;
            channelAvailableGhz5 = binding.channelAvailableGhz5;
        }

        Binding(@NonNull View view) {
            root = view;
            channelAvailableCountry = view.findViewById(R.id.channel_available_country);
            channelAvailableTitleGhz2 = view.findViewById(R.id.channel_available_title_ghz_2);
            channelAvailableGhz2 = view.findViewById(R.id.channel_available_ghz_2);
            channelAvailableTitleGhz5 = view.findViewById(R.id.channel_available_title_ghz_5);
            channelAvailableGhz5 = view.findViewById(R.id.channel_available_ghz_5);
        }

        @NonNull
        View getRoot() {
            return root;
        }

        @NonNull
        TextView getChannelAvailableCountry() {
            return channelAvailableCountry;
        }

        @NonNull
        TextView getChannelAvailableTitleGhz2() {
            return channelAvailableTitleGhz2;
        }

        @NonNull
        TextView getChannelAvailableGhz2() {
            return channelAvailableGhz2;
        }

        @NonNull
        TextView getChannelAvailableTitleGhz5() {
            return channelAvailableTitleGhz5;
        }

        @NonNull
        TextView getChannelAvailableGhz5() {
            return channelAvailableGhz5;
        }
    }

}
