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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<WifiInfo> {

    public ListViewAdapter(Activity activity, List<WifiInfo> wifiInfoResults) {
        super(activity, android.R.layout.simple_list_item_1, wifiInfoResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.column_row, null);
        }

        WifiInfo wifiInfo = getItem(position);

        ((TextView) convertView.findViewById(R.id.ssid)).setText(
                (TextUtils.isEmpty(wifiInfo.getSSID()) ? "HIDDEN" : wifiInfo.getSSID()) + " (" + wifiInfo.getBSSID() + ")");

        WifiLevel wifiLevel = wifiInfo.getWifiLevel();

        ImageView imageView = (ImageView) convertView.findViewById(R.id.levelImage);
        imageView.setImageResource(wifiLevel.getImageResource());
        imageView.setColorFilter(getContext().getResources().getColor(wifiLevel.getColorResource()));

        ImageView securityImage = (ImageView) convertView.findViewById(R.id.securityImage);
        securityImage.setImageResource(wifiInfo.getSecurity().getImageResource());

        TextView textLevel = (TextView) convertView.findViewById(R.id.level);
        textLevel.setText(wifiInfo.getLevel() + "dBm");
        textLevel.setTextColor(getContext().getResources().getColor(wifiLevel.getColorResource()));

        ((TextView) convertView.findViewById(R.id.channel)).setText("" + wifiInfo.getChannel());
        ((TextView) convertView.findViewById(R.id.frequency)).setText(" (" + wifiInfo.getFrequency().getBand()+")");
        ((TextView) convertView.findViewById(R.id.security)).setText(securitiesAsString(wifiInfo.getSecurities()));

        return convertView;
    }

    private String securitiesAsString(List<Security> securities) {
        StringBuilder result = new StringBuilder();
        for (Security current: securities) {
            result.append(current.name());
            result.append(" ");
        }
        return result.toString();
    }
}
