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

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.wifi.Connection;
import com.vrem.wifianalyzer.wifi.Details;
import com.vrem.wifianalyzer.wifi.DetailsInfo;
import com.vrem.wifianalyzer.wifi.Security;
import com.vrem.wifianalyzer.wifi.Strength;
import com.vrem.wifianalyzer.wifi.Updater;
import com.vrem.wifianalyzer.wifi.WiFiData;

import org.apache.commons.lang3.StringUtils;

public class ListViewAdapter extends BaseExpandableListAdapter implements Updater {

    private final AppCompatActivity activity;
    private final View headerView;
    private final Resources resources;
    private ExpandableListView expandableListView;
    private WiFiData wifiData;

    public ListViewAdapter(@NonNull AppCompatActivity appCompatActivity) {
        super();
        activity = appCompatActivity;
        resources = activity.getResources();
        headerView = activity.findViewById(R.id.contentHeader);
        wifiData = new WiFiData();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = getView(convertView);
        Details details = (Details) getGroup(groupPosition);
        setView(convertView, details);

        convertView.findViewById(R.id.tab).setVisibility(View.GONE);
        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.groupIndicator);
        TextView groupCount = (TextView) convertView.findViewById(R.id.groupCount);
        int childrenCount = getChildrenCount(groupPosition);
        if (childrenCount > 0) {
            groupIndicator.setVisibility(View.VISIBLE);
            groupIndicator.setImageResource(
                    isExpanded ? R.drawable.ic_expand_less_black_24dp : R.drawable.ic_expand_more_black_24dp);
            groupCount.setVisibility(View.VISIBLE);
            groupCount.setText(String.format("(%d) ", childrenCount));
        } else {
            groupIndicator.setVisibility(View.GONE);
            groupCount.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = getView(convertView);
        Details details = (Details) getChild(groupPosition, childPosition);
        setView(convertView, details);

        convertView.setBackgroundColor(resources.getColor(R.color.shadow_mid_color));
        convertView.findViewById(R.id.tab).setVisibility(View.VISIBLE);
        convertView.findViewById(R.id.groupIndicator).setVisibility(View.GONE);
        convertView.findViewById(R.id.groupCount).setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public void update(@NonNull WiFiData wifiData) {
        this.wifiData = wifiData;
        if (expandableListView != null) {
            for (int i = 0; i < getGroupCount(); i++) {
                expandableListView.collapseGroup(i);
            }
            header();
        }
        notifyDataSetChanged();
    }

    private void header() {
        Connection connection = wifiData.connection();

        if (connection.connected() && connection.hasDetails()) {
            setView(headerView, connection.detailsInfo());

            TextView ssid = (TextView) headerView.findViewById(R.id.ssid);
            ssid.setText(String.format("%s %s", ssid.getText(), connection.ipAddress()));
            ssid.setTextColor(resources.getColor(R.color.connected));

            headerView.setVisibility(View.VISIBLE);
        } else {
            headerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getGroupCount() {
        return wifiData.parentsCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return wifiData.childrenCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return wifiData.parent(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return wifiData.child(groupPosition, childPosition);
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

    private View getView(View view) {
        if (view != null) {
            return view;
        }
        LayoutInflater inflater = activity.getLayoutInflater();
        return inflater.inflate(R.layout.content_details, null);
    }

    void setExpandableListView(@NonNull ExpandableListView expandableListView) {
        this.expandableListView = expandableListView;
    }

    private void setView(@NonNull View view, @NonNull DetailsInfo detailsInfo) {
        String ssid = (StringUtils.isBlank(detailsInfo.SSID()) ? "***" : detailsInfo.SSID());
        ((TextView) view.findViewById(R.id.ssid)).setText(String.format("%s (%s)", ssid, detailsInfo.BSSID()));

        Strength strength = detailsInfo.strength();
        ImageView imageView = (ImageView) view.findViewById(R.id.levelImage);
        imageView.setImageResource(strength.imageResource());
        imageView.setColorFilter(resources.getColor(strength.colorResource()));

        Security security = detailsInfo.security();
        ((ImageView) view.findViewById(R.id.securityImage)).setImageResource(security.imageResource());

        TextView textLevel = (TextView) view.findViewById(R.id.level);
        textLevel.setText(String.format("%ddBm", detailsInfo.level()));
        textLevel.setTextColor(resources.getColor(strength.colorResource()));

        ((TextView) view.findViewById(R.id.channel)).setText(String.format("%d", detailsInfo.channel()));
        ((TextView) view.findViewById(R.id.frequency)).setText(String.format("(%dMHz)", detailsInfo.frequency()));
        ((TextView) view.findViewById(R.id.distance)).setText(String.format("%6.2fm", detailsInfo.distance()));
        ((TextView) view.findViewById(R.id.capabilities)).setText(detailsInfo.capabilities());

        TextView textVendor = ((TextView) view.findViewById(R.id.vendor));
        String vendor = detailsInfo.vendorName();
        if (StringUtils.isBlank(vendor)) {
            textVendor.setVisibility(View.GONE);
        } else {
            textVendor.setVisibility(View.VISIBLE);
            textVendor.setText(vendor);
        }
    }

}
