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

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.wifi.Details;
import com.vrem.wifianalyzer.wifi.Security;
import com.vrem.wifianalyzer.wifi.Strength;

import java.util.List;

public class ListViewAdapter extends BaseListViewAdapter<Details,String> {

    public ListViewAdapter(@NonNull AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = getView(convertView, R.layout.list_group);

        Details details = getGroupData(groupPosition);
        Strength strength = details.getWifiLevel();
        Security security = details.getSecurity();

        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.groupIndicator);
        groupIndicator.setImageResource(isExpanded ? R.drawable.ic_expand_less_black_24dp : R.drawable.ic_expand_more_black_24dp);

        ((TextView) convertView.findViewById(R.id.ssid)).setText((TextUtils.isEmpty(details.getSSID()) ? "HIDDEN" : details.getSSID()));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.levelImage);
        imageView.setImageResource(strength.getImageResource());
        imageView.setColorFilter(convertView.getResources().getColor(strength.getColorResource()));

        ((ImageView) convertView.findViewById(R.id.securityImage)).setImageResource(security.getImageResource());

        TextView textLevel = (TextView) convertView.findViewById(R.id.level);
        textLevel.setText(details.getLevel() + "dBm");
        textLevel.setTextColor(getActivity().getResources().getColor(strength.getColorResource()));

        ((TextView) convertView.findViewById(R.id.channel)).setText("" + details.getChannel());
        ((TextView) convertView.findViewById(R.id.frequency)).setText(" (" + details.getFrequency().getBand() + ")");
        ((TextView) convertView.findViewById(R.id.security)).setText(securitiesAsString(details.getSecurities()));

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

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = getView(convertView, R.layout.list_item);

        String childText = getChildData(groupPosition, childPosition);
        TextView txtListChild = (TextView) convertView.findViewById(R.id.item);

        txtListChild.setText(childText);
        return convertView;
    }
}
