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
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.DetailsInfo;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import java.util.ArrayList;
import java.util.List;

class ChannelListAdapter extends ArrayAdapter<DetailsInfo> implements UpdateNotifier {

    private final MainContext mainContext = MainContext.INSTANCE;
    private final Resources resources;

    ChannelListAdapter(@NonNull Context context) {
        super(context, R.layout.channel_details, new ArrayList<DetailsInfo>());
        this.resources = context.getResources();
        mainContext.getScanner().addUpdateNotifier(this);
    }

    @Override
    public void update(WiFiData wifiData) {
        clear();
        addAll(wifiData.getWiFiChannelList());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mainContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.channel_details, parent, false);
        }
        DetailsInfo item = getItem(position);
        int count = item.getChildren().size();
        if (!item.isConnected()) {
            boolean found = false;
            List<DetailsInfo> children = item.getChildren();
            for (DetailsInfo child : children) {
                if (child.isConnected()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                count++;
            }
        }
        int color = R.color.success_color;
        if (count > 3) {
            color = R.color.error_color;
        } else if (count > 1) {
            color = R.color.warning_color;
        }
        ((TextView) convertView.findViewById(R.id.channelNumber)).setText("" + item.getChannel());
        ((TextView) convertView.findViewById(R.id.channelWiFiCount)).setText("" + (count));
        ((ImageView) convertView.findViewById(R.id.channelImage)).setColorFilter(resources.getColor(color));
        return convertView;
    }
}
