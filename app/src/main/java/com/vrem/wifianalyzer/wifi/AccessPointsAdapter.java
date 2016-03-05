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
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import java.util.ArrayList;
import java.util.List;

class AccessPointsAdapter extends BaseExpandableListAdapter implements UpdateNotifier {

    private final MainContext mainContext = MainContext.INSTANCE;
    private final Resources resources;
    private final Data data;
    private final AccessPointsDetail accessPointsDetail;

    AccessPointsAdapter(@NonNull Context context) {
        super();
        this.resources = context.getResources();
        this.data = new Data();
        this.accessPointsDetail = new AccessPointsDetail();
        mainContext.getScanner().addUpdateNotifier(this);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = getView(convertView, parent);
        WiFiDetail details = (WiFiDetail) getGroup(groupPosition);
        accessPointsDetail.setView(resources, convertView, details, false);

        int childrenCount = getChildrenCount(groupPosition);
        if (childrenCount > 0) {
            convertView.findViewById(R.id.groupColumn).setVisibility(View.VISIBLE);
            ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.groupIndicator);
            groupIndicator.setImageResource(isExpanded
                    ? R.drawable.ic_expand_less_black_24dp
                    : R.drawable.ic_expand_more_black_24dp);
            groupIndicator.setColorFilter(resources.getColor(R.color.icons_color));
            ((TextView) convertView.findViewById(R.id.groupCount)).setText(String.format("(%d) ", childrenCount + 1));
        } else {
            convertView.findViewById(R.id.groupColumn).setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = getView(convertView, parent);
        WiFiDetail details = (WiFiDetail) getChild(groupPosition, childPosition);
        accessPointsDetail.setView(resources, convertView, details, true);
        convertView.findViewById(R.id.groupColumn).setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        data.update(wiFiData);
        notifyDataSetChanged();
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

    private View getView(View convertView, ViewGroup parentView) {
        if (convertView != null) {
            return convertView;
        }

        LayoutInflater inflater = mainContext.getLayoutInflater();
        return inflater.inflate(R.layout.access_points_details, null);
    }

    class Data {
        WiFiDetail connection;
        List<WiFiDetail> wiFiDetails = new ArrayList<>();

        void update(WiFiData wiFiData) {
            if (wiFiData != null) {
                connection = wiFiData.getConnection();
                Settings settings = mainContext.getSettings();
                wiFiDetails = wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy(), settings.getGroupBy());
            }
        }

        int parentsCount() {
            return wiFiDetails.size();
        }

        boolean validParentIndex(int index) {
            return index >= 0 && index < parentsCount();
        }

        boolean validChildrenIndex(int indexParent, int indexChild) {
            return validParentIndex(indexParent) && indexChild >= 0 && indexChild < childrenCount(indexParent);
        }

        WiFiDetail parent(int index) {
            return validParentIndex(index) ? wiFiDetails.get(index) : null;
        }

        int childrenCount(int index) {
            return validParentIndex(index) ? wiFiDetails.get(index).getChildren().size() : 0;
        }

        WiFiDetail child(int indexParent, int indexChild) {
            return validChildrenIndex(indexParent, indexChild) ? wiFiDetails.get(indexParent).getChildren().get(indexChild) : null;
        }
    }

}
