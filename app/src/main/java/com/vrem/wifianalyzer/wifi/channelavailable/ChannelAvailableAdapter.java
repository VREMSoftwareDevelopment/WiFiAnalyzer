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

package com.vrem.wifianalyzer.wifi.channelavailable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import java.util.List;
import java.util.Locale;

class ChannelAvailableAdapter extends ArrayAdapter<WiFiChannelCountry> {
    private static final String SEPARATOR = ",";

    ChannelAvailableAdapter(@NonNull Context context, @NonNull List<WiFiChannelCountry> wiFiChannelCountries) {
        super(context, R.layout.channel_available_details, wiFiChannelCountries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = MainContext.INSTANCE.getMainActivity().getLayoutInflater();
            view = layoutInflater.inflate(R.layout.channel_available_details, parent, false);
        }

        WiFiChannelCountry wiFiChannelCountry = getItem(position);
        ((TextView) view.findViewById(R.id.channel_available_country))
            .setText(wiFiChannelCountry.getCountryCode() + " - " + wiFiChannelCountry.getCountryName());
        ((TextView) view.findViewById(R.id.channel_available_title_ghz_2))
            .setText(String.format(Locale.ENGLISH, "%s : ",
                view.getResources().getString(WiFiBand.GHZ2.getTextResource())));
        ((TextView) view.findViewById(R.id.channel_available_ghz_2))
            .setText(TextUtils.join(SEPARATOR, wiFiChannelCountry.getChannelsGHZ2().toArray()));
        ((TextView) view.findViewById(R.id.channel_available_title_ghz_5))
            .setText(String.format(Locale.ENGLISH, "%s : ",
                view.getResources().getString(WiFiBand.GHZ5.getTextResource())));
        ((TextView) view.findViewById(R.id.channel_available_ghz_5))
            .setText(TextUtils.join(SEPARATOR, wiFiChannelCountry.getChannelsGHZ5().toArray()));
        return view;
    }

}
