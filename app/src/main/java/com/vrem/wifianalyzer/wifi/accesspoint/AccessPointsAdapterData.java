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
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.predicate.FilterPredicate;

import org.apache.commons.collections4.Predicate;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

class AccessPointsAdapterData {
    private AccessPointsAdapterGroup accessPointsAdapterGroup;
    private List<WiFiDetail> wiFiDetails;

    AccessPointsAdapterData() {
        wiFiDetails = Collections.emptyList();
        setAccessPointsAdapterGroup(new AccessPointsAdapterGroup());
    }

    void update(@NonNull WiFiData wiFiData, ExpandableListView expandableListView) {
        Settings settings = MainContext.INSTANCE.getSettings();
        Predicate<WiFiDetail> predicate = FilterPredicate.makeAccessPointsPredicate(settings);
        wiFiDetails = wiFiData.getWiFiDetails(predicate, settings.getSortBy(), settings.getGroupBy());
        accessPointsAdapterGroup.update(wiFiDetails, expandableListView);
    }

    int parentsCount() {
        return wiFiDetails.size();
    }

    private boolean validParentIndex(int index) {
        return index >= 0 && index < parentsCount();
    }

    private boolean validChildrenIndex(int indexParent, int indexChild) {
        return validParentIndex(indexParent) && indexChild >= 0 && indexChild < childrenCount(indexParent);
    }

    WiFiDetail parent(int index) {
        return validParentIndex(index) ? wiFiDetails.get(index) : WiFiDetail.EMPTY;
    }

    int childrenCount(int index) {
        return validParentIndex(index) ? wiFiDetails.get(index).getChildren().size() : 0;
    }

    WiFiDetail child(int indexParent, int indexChild) {
        return validChildrenIndex(indexParent, indexChild) ? wiFiDetails.get(indexParent).getChildren().get(indexChild) : WiFiDetail.EMPTY;
    }

    void onGroupCollapsed(int groupPosition) {
        accessPointsAdapterGroup.onGroupCollapsed(wiFiDetails, groupPosition);
    }

    void onGroupExpanded(int groupPosition) {
        accessPointsAdapterGroup.onGroupExpanded(wiFiDetails, groupPosition);
    }

    void setAccessPointsAdapterGroup(@NonNull AccessPointsAdapterGroup accessPointsAdapterGroup) {
        this.accessPointsAdapterGroup = accessPointsAdapterGroup;
    }

    List<WiFiDetail> getWiFiDetails() {
        return wiFiDetails;
    }
}
