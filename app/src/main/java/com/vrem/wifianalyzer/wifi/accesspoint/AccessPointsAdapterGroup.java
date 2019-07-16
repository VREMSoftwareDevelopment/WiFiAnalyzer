/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.accesspoint;

import android.widget.ExpandableListView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

class AccessPointsAdapterGroup {
    private final Set<String> expanded;
    private GroupBy groupBy;

    AccessPointsAdapterGroup() {
        expanded = new HashSet<>();
        groupBy = null;
    }

    void update(@NonNull List<WiFiDetail> wiFiDetails, ExpandableListView expandableListView) {
        updateGroupBy();
        if (isGroupExpandable() && expandableListView != null) {
            int groupCount = expandableListView.getExpandableListAdapter().getGroupCount();
            for (int i = 0; i < groupCount; i++) {
                WiFiDetail wiFiDetail = getWiFiDetail(wiFiDetails, i);
                if (expanded.contains(getGroupExpandKey(wiFiDetail))) {
                    expandableListView.expandGroup(i);
                } else {
                    expandableListView.collapseGroup(i);
                }
            }
        }
    }

    void updateGroupBy() {
        GroupBy currentGroupBy = MainContext.INSTANCE.getSettings().getGroupBy();
        if (!currentGroupBy.equals(this.groupBy)) {
            expanded.clear();
            this.groupBy = currentGroupBy;
        }
    }

    GroupBy getGroupBy() {
        return groupBy;
    }


    void onGroupCollapsed(@NonNull List<WiFiDetail> wiFiDetails, int groupPosition) {
        if (isGroupExpandable()) {
            WiFiDetail wiFiDetail = getWiFiDetail(wiFiDetails, groupPosition);
            if (wiFiDetail.noChildren()) {
                expanded.remove(getGroupExpandKey(wiFiDetail));
            }
        }
    }

    void onGroupExpanded(@NonNull List<WiFiDetail> wiFiDetails, int groupPosition) {
        if (isGroupExpandable()) {
            WiFiDetail wiFiDetail = getWiFiDetail(wiFiDetails, groupPosition);
            if (wiFiDetail.noChildren()) {
                expanded.add(getGroupExpandKey(wiFiDetail));
            }
        }
    }

    boolean isGroupExpandable() {
        return GroupBy.SSID.equals(this.groupBy) || GroupBy.CHANNEL.equals(this.groupBy);
    }

    String getGroupExpandKey(@NonNull WiFiDetail wiFiDetail) {
        String result = StringUtils.EMPTY;
        if (GroupBy.SSID.equals(this.groupBy)) {
            result = wiFiDetail.getSSID();
        }
        if (GroupBy.CHANNEL.equals(this.groupBy)) {
            result += Integer.toString(wiFiDetail.getWiFiSignal().getPrimaryWiFiChannel().getChannel());
        }
        return result;
    }

    Set<String> getExpanded() {
        return expanded;
    }

    private WiFiDetail getWiFiDetail(@NonNull List<WiFiDetail> wiFiDetails, int index) {
        try {
            return wiFiDetails.get(index);
        } catch (IndexOutOfBoundsException e) {
            return WiFiDetail.EMPTY;
        }
    }

}
