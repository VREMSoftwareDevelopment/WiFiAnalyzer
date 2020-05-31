/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.widget.ExpandableListView
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext.INSTANCE
import com.vrem.wifianalyzer.wifi.model.GroupBy
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

@OpenClass
class AccessPointsAdapterGroup(val expanded: MutableSet<String> = mutableSetOf(), var groupBy: GroupBy = GroupBy.NONE) {

    fun update(wiFiDetails: List<WiFiDetail>, expandableListView: ExpandableListView?) {
        updateGroupBy()
        expandableListView?.let {
            if (!groupBy.none) {
                (0 until it.expandableListAdapter.groupCount).forEach { index ->
                    val wiFiDetail = wiFiDetails.getOrNull(index) ?: WiFiDetail.EMPTY
                    if (expanded.contains(groupBy.group(wiFiDetail))) {
                        expandableListView.expandGroup(index)
                    } else {
                        expandableListView.collapseGroup(index)
                    }
                }
            }
        }
    }

    fun updateGroupBy() {
        val currentGroupBy = INSTANCE.settings.groupBy()
        if (currentGroupBy != groupBy) {
            expanded.clear()
            groupBy = currentGroupBy
        }
    }

    fun onGroupCollapsed(wiFiDetails: List<WiFiDetail>, groupPosition: Int) {
        if (!groupBy.none) {
            val wiFiDetail = wiFiDetails.getOrNull(groupPosition) ?: WiFiDetail.EMPTY
            if (wiFiDetail.noChildren()) {
                expanded.remove(groupBy.group(wiFiDetail))
            }
        }
    }

    fun onGroupExpanded(wiFiDetails: List<WiFiDetail>, groupPosition: Int) {
        if (!groupBy.none) {
            val wiFiDetail = wiFiDetails.getOrNull(groupPosition) ?: WiFiDetail.EMPTY
            if (wiFiDetail.noChildren()) {
                expanded.add(groupBy.group(wiFiDetail))
            }
        }
    }

}