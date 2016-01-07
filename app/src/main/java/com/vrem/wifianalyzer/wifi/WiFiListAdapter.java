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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.Details;
import com.vrem.wifianalyzer.wifi.model.DetailsInfo;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class WiFiListAdapter extends BaseExpandableListAdapter implements UpdateNotifier {

    private final View headerView;
    private final Resources resources;
    private final Data data;
    private MainContext mainContext = MainContext.INSTANCE;

    public WiFiListAdapter(@NonNull View headerView, @NonNull Context context) {
        super();
        this.resources = context.getResources();
        this.headerView = headerView;
        this.data = new Data();
        mainContext.getScanner().addUpdateNotifier(this);
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
            groupIndicator.setImageResource(isExpanded
                    ? R.drawable.ic_expand_less_black_24dp
                    : R.drawable.ic_expand_more_black_24dp);
            groupIndicator.setColorFilter(resources.getColor(R.color.icons_color));
            groupCount.setVisibility(View.VISIBLE);
            groupCount.setText(String.format("(%d) ", childrenCount + 1));
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
    public void update(WiFiData wifiData) {
        data.update(wifiData);
        header();
        notifyDataSetChanged();
    }

    private void header() {
        if (data.connection != null) {
            setView(headerView, data.connection);
            headerView.setVisibility(View.VISIBLE);
        } else {
            headerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getGroupCount() {
        return data.parentsCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.childrenCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.parent(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.child(groupPosition, childPosition);
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

        LayoutInflater inflater = mainContext.getLayoutInflater();
        return inflater.inflate(R.layout.main_content_details, null);
    }

    private void setView(@NonNull View view, @NonNull DetailsInfo detailsInfo) {

        ((TextView) view.findViewById(R.id.ssid)).setText(
                String.format("%s (%s)",
                        StringUtils.isBlank(detailsInfo.getSSID()) ? "***" : detailsInfo.getSSID(),
                        detailsInfo.getBSSID()));

        TextView textIPAddress = (TextView) view.findViewById(R.id.ipAddress);
        String ipAddress = detailsInfo.getIPAddress();
        if (StringUtils.isBlank(ipAddress)) {
            textIPAddress.setVisibility(View.GONE);
        } else {
            textIPAddress.setVisibility(View.VISIBLE);
            textIPAddress.setText(ipAddress);
        }

        Strength strength = detailsInfo.getStrength();
        ImageView imageView = (ImageView) view.findViewById(R.id.levelImage);
        imageView.setImageResource(strength.imageResource());
        imageView.setColorFilter(resources.getColor(strength.colorResource()));

        Security security = detailsInfo.getSecurity();
        ImageView securityImage = (ImageView) view.findViewById(R.id.securityImage);
        securityImage.setImageResource(security.imageResource());
        securityImage.setColorFilter(resources.getColor(R.color.icons_color));

        TextView textLevel = (TextView) view.findViewById(R.id.level);
        textLevel.setText(String.format("%ddBm", detailsInfo.getLevel()));
        textLevel.setTextColor(resources.getColor(strength.colorResource()));

        ((TextView) view.findViewById(R.id.channel)).setText(String.format("%d", detailsInfo.getChannel()));
        ((TextView) view.findViewById(R.id.frequency)).setText(String.format("(%dMHz)", detailsInfo.getFrequency()));
        ((TextView) view.findViewById(R.id.distance)).setText(String.format("%6.2fm", detailsInfo.getDistance()));
        ((TextView) view.findViewById(R.id.capabilities)).setText(detailsInfo.getCapabilities());

        TextView textVendor = ((TextView) view.findViewById(R.id.vendor));
        String vendor = detailsInfo.getVendorName();
        if (StringUtils.isBlank(vendor)) {
            textVendor.setVisibility(View.GONE);
        } else {
            textVendor.setVisibility(View.VISIBLE);
            textVendor.setText(vendor);
        }
    }

    class Data {
        DetailsInfo connection;
        List<DetailsInfo> wifiList = new ArrayList<>();

        void update(WiFiData wifiData) {
            if (wifiData != null) {
                connection = wifiData.getConnection();
                wifiList = wifiData.getWiFiList();
            }
        }

        int parentsCount() {
            return wifiList.size();
        }

        boolean validParentIndex(int index) {
            return index >= 0 && index < parentsCount();
        }

        boolean validChildrenIndex(int indexParent, int indexChild) {
            return validParentIndex(indexParent) && indexChild >= 0 && indexChild < childrenCount(indexParent);
        }

        DetailsInfo parent(int index) {
            return validParentIndex(index) ? wifiList.get(index) : null;
        }

        int childrenCount(int index) {
            return validParentIndex(index) ? wifiList.get(index).getChildren().size() : 0;
        }

        DetailsInfo child(int indexParent, int indexChild) {
            return validChildrenIndex(indexParent, indexChild) ? wifiList.get(indexParent).getChildren().get(indexChild) : null;
        }
    }

}
