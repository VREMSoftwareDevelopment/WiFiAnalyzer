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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.wifi.Connection;
import com.vrem.wifianalyzer.wifi.Details;
import com.vrem.wifianalyzer.wifi.Security;
import com.vrem.wifianalyzer.wifi.Strength;
import com.vrem.wifianalyzer.wifi.Updater;
import com.vrem.wifianalyzer.wifi.WifiInformation;

import java.text.DecimalFormat;

public class ListViewAdapter extends BaseExpandableListAdapter implements Updater {

    private final AppCompatActivity activity;
    private final View headerView;
    private final DecimalFormat distanceFormat;
    private final Resources resources;
    private ExpandableListView expandableListView;
    private WifiInformation wifiInformation;

    public ListViewAdapter(@NonNull AppCompatActivity activity) {
        super();
        this.activity = activity;
        this.resources = activity.getResources();
        this.headerView = getView(null, R.layout.content_header);
        this.distanceFormat = new DecimalFormat("#.##");
        this.wifiInformation = new WifiInformation();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = getView(convertView, R.layout.content_details);

        Details details = (Details) getGroup(groupPosition);

        convertView.findViewById(R.id.tab).setVisibility(View.GONE);

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

        convertView.setBackgroundColor(resources.getColor(R.color.shadow_mid_color));

        Details details = (Details) getChild(groupPosition, childPosition);

        convertView.findViewById(R.id.groupIndicator).setVisibility(View.GONE);

        return getView(details, convertView);
    }

    private View getView(Details details, View convertView) {
        convertView.findViewById(R.id.connected).setVisibility(details.isConnected() ? View.VISIBLE : View.GONE);

        String ssid = (TextUtils.isEmpty(details.getSSID()) ? "HIDDEN" : details.getSSID());
        ((TextView) convertView.findViewById(R.id.ssid)).setText(ssid + " (" + details.getBSSID() + ")");

        Strength strength = details.getStrength();
        ImageView imageView = (ImageView) convertView.findViewById(R.id.levelImage);
        imageView.setImageResource(strength.getImageResource());
        imageView.setColorFilter(resources.getColor(strength.getColorResource()));

        Security security = details.getSecurity();
        ((ImageView) convertView.findViewById(R.id.securityImage)).setImageResource(security.getImageResource());

        TextView textLevel = (TextView) convertView.findViewById(R.id.level);
        textLevel.setText(details.getLevel() + "dBm");
        textLevel.setTextColor(resources.getColor(strength.getColorResource()));

        ((TextView) convertView.findViewById(R.id.channel)).setText(""+details.getChannel());
        ((TextView) convertView.findViewById(R.id.frequency)).setText("("+details.getFrequency() + "MHz)");
        ((TextView) convertView.findViewById(R.id.distance)).setText(distanceFormat.format(details.getDistance()) + "m");
        ((TextView) convertView.findViewById(R.id.capabilities)).setText(details.getCapabilities());

        return convertView;
    }

    @Override
    public void update(@NonNull WifiInformation wifiInformation) {
        this.wifiInformation = wifiInformation;
        if (this.expandableListView != null) {
            for (int i = 0; i < getGroupCount(); i++) {
                this.expandableListView.collapseGroup(i);
            }
            addHeader();
        }
        notifyDataSetChanged();
    }

    private void addHeader() {
        Connection connection = this.wifiInformation.getConnection();
        if (!connection.isConnected()) {
            if (this.expandableListView.getHeaderViewsCount() > 0) {
                this.expandableListView.removeHeaderView(this.headerView);
            }
            return;
        }
        if (this.expandableListView.getHeaderViewsCount() == 0) {
            this.expandableListView.addHeaderView(this.headerView);
        }
        headerView.setBackgroundColor(resources.getColor(R.color.header));
        headerView.setPadding(
                resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin),
                0,
                resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin),
                0);

        headerView.findViewById(R.id.tab).setVisibility(View.GONE);
        headerView.findViewById(R.id.connected).setVisibility(View.GONE);
        TextView ssid = (TextView) headerView.findViewById(R.id.ssid);
        ssid.setText(connection.getSSID() + " (" + connection.getBSSID() + ")");
        ssid.setTextColor(resources.getColor(R.color.connected));

        Strength strength = connection.getStrength();
        ImageView imageView = (ImageView) headerView.findViewById(R.id.levelImage);
        imageView.setImageResource(strength.getImageResource());
        imageView.setColorFilter(resources.getColor(strength.getColorResource()));

        TextView textLevel = (TextView) headerView.findViewById(R.id.level);
        textLevel.setText(connection.getRssi() + "dBm");
        textLevel.setTextColor(resources.getColor(strength.getColorResource()));

        ((TextView) headerView.findViewById(R.id.channel)).setText("" + connection.getChannel());
        ((TextView) headerView.findViewById(R.id.frequency)).setText("("+connection.getFrequency() + "MHz)");
        ((TextView) headerView.findViewById(R.id.distance)).setText(distanceFormat.format(connection.getDistance()) + "m");

        TextView ipAddress = (TextView) headerView.findViewById(R.id.capabilities);
        ipAddress.setText(connection.getIpAddress());
        ipAddress.setTextColor(resources.getColor(R.color.connected));
    }

    @Override
    public int getGroupCount() {
        return this.wifiInformation.getParentsSize();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.wifiInformation.getChildrenSize(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.wifiInformation.getParent(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.wifiInformation.getChild(groupPosition, childPosition);
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
