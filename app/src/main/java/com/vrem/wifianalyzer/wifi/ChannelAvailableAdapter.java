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
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import java.util.List;

class ChannelAvailableAdapter extends ArrayAdapter<WiFiChannelCountry> {

    ChannelAvailableAdapter(@NonNull Context context, @NonNull List<WiFiChannelCountry> wiFiChannelCountries) {
        super(context, R.layout.channel_available_details, wiFiChannelCountries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = MainContext.INSTANCE.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.channel_available_details, parent, false);
        }
        WiFiChannelCountry wiFiChannelCountry = getItem(position);
        ((TextView) convertView.findViewById(R.id.channel_available_country)).setText(wiFiChannelCountry.getCountryCode() + " - " + wiFiChannelCountry.getCountryName());
        ((TextView) convertView.findViewById(R.id.channel_available_ghz_2)).setText(getChannels(wiFiChannelCountry.getChannelsGHZ_2()));
        ((TextView) convertView.findViewById(R.id.channel_available_ghz_5)).setText(getChannels(wiFiChannelCountry.getChannelsGHZ_5()));
        return convertView;
    }

    String getChannels(List<Integer> channels) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int channel : channels) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(channel);
        }
        return stringBuilder.toString();
    }
}
