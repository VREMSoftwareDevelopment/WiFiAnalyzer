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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.wifi.Details;
import com.vrem.wifianalyzer.wifi.Information;
import com.vrem.wifianalyzer.wifi.Security;
import com.vrem.wifianalyzer.wifi.Strength;
import com.vrem.wifianalyzer.wifi.Updater;

import java.text.DecimalFormat;

public class ListViewAdapter extends BaseExpandableListAdapter implements Updater {

    private final AppCompatActivity activity;
    private Information information = new Information();
    private ExpandableListView expandableListView;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public ListViewAdapter(@NonNull AppCompatActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = getView(convertView, R.layout.content_details);

        Details details = (Details) getGroup(groupPosition);

        TextView tab = (TextView) convertView.findViewById(R.id.tab);
        tab.setVisibility(View.GONE);

        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.groupIndicator);

        if (getChildrenCount(groupPosition) > 0) {
            groupIndicator.setVisibility(View.VISIBLE);
            groupIndicator.setImageResource(
                    isExpanded ? R.drawable.ic_expand_less_black_24dp : R.drawable.ic_expand_more_black_24dp);
        } else {
            groupIndicator.setVisibility(View.GONE);
        }

        return getView(details, convertView);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = getView(convertView, R.layout.content_details);

        convertView.setBackgroundColor(activity.getResources().getColor(R.color.shadow_mid_color));

        Details details = (Details) getChild(groupPosition, childPosition);

        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.groupIndicator);
        groupIndicator.setVisibility(View.GONE);

        return getView(details, convertView);
    }

    private View getView(Details details, View convertView) {
        Strength strength = details.getStrength();
        Security security = details.getSecurity();
        String ssid = (TextUtils.isEmpty(details.getSSID()) ? "HIDDEN" : details.getSSID());

        ((TextView) convertView.findViewById(R.id.ssid)).setText(ssid + " (" + details.getBSSID() + ")");

        ImageView imageView = (ImageView) convertView.findViewById(R.id.levelImage);
        imageView.setImageResource(strength.getImageResource());
        imageView.setColorFilter(convertView.getResources().getColor(strength.getColorResource()));

        ((ImageView) convertView.findViewById(R.id.securityImage)).setImageResource(security.getImageResource());

        TextView textLevel = (TextView) convertView.findViewById(R.id.level);
        textLevel.setText(details.getLevel() + "dBm");
        textLevel.setTextColor(activity.getResources().getColor(strength.getColorResource()));

        ((TextView) convertView.findViewById(R.id.channel)).setText("" + details.getChannel());
        ((TextView) convertView.findViewById(R.id.frequency)).setText(" (" + details.getFrequency() + "MHz)");
        ((TextView) convertView.findViewById(R.id.distance)).setText(decimalFormat.format(details.getDistance()) + "m");
        ((TextView) convertView.findViewById(R.id.capabilities)).setText(details.getCapabilities());

        return convertView;
    }

    @Override
    public void update(@NonNull Information information) {
        this.information = information;
        notifyDataSetChanged();
        if (expandableListView != null) {
            for (int i = 0; i < getGroupCount(); i++) {
                this.expandableListView.collapseGroup(i);
            }
        }
    }

    @Override
    public int getGroupCount() {
        return this.information.getParentsSize();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.information.getChildrenSize(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.information.getParent(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.information.getChild(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View getView(View view, int resource) {
        if (view != null) {
            return view;
        }
        LayoutInflater inflater = activity.getLayoutInflater();
        return inflater.inflate(resource, null);
    }

    void setExpandableListView(ExpandableListView expandableListView) {
        this.expandableListView = expandableListView;
    }
}
