/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<WifiScan> {

    public ListViewAdapter(Activity activity, List<WifiScan> wifiScanResults) {
        super(activity, android.R.layout.simple_list_item_1, wifiScanResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.column_row, null);
        }

        WifiScan wifiScan = getItem(position);

        ((TextView) convertView.findViewById(R.id.ssid)).setText(
                (wifiScan.getSSID().length() == 0 ? "HIDDEN" : wifiScan.getSSID()) + " (" + wifiScan.getBSSID() + ")");

        WifiLevel wifiLevel = wifiScan.getWifiLevel();
        Security security = wifiScan.getSecurity();
        String securities = wifiScan.getSecurities();
        Frequency frequency = wifiScan.getFrequency();
        int channel = wifiScan.getChannel();

        ImageView imageView = (ImageView) convertView.findViewById(R.id.levelImage);
        imageView.setImageResource(wifiLevel.getImageResource());

        ImageView securityImage = (ImageView) convertView.findViewById(R.id.securityImage);
        securityImage.setImageResource(security.getImageResource());

        TextView textLevel = (TextView) convertView.findViewById(R.id.level);
        textLevel.setText(wifiScan.getLevel() + "dBm");
        textLevel.setTextColor(getContext().getResources().getColor(wifiLevel.getColorResource()));

        ((TextView) convertView.findViewById(R.id.channel)).setText("" + channel);
        ((TextView) convertView.findViewById(R.id.frequency)).setText(" (" + frequency.getBand()+")");
        ((TextView) convertView.findViewById(R.id.security)).setText(securities);

        return convertView;
    }
}
