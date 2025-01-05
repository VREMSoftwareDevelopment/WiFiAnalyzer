/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.wifi.model.GroupBy
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

@OpenClass
class AccessPointsAdapterGroup(val expanded: MutableSet<String> = mutableSetOf(), var groupBy: GroupBy = GroupBy.NONE) {

    fun update(wiFiDetails: List<WiFiDetail>, expandableListView: ExpandableListView?) {
        updateGroupBy()
        if (!groupBy.none) {
            expandableListView?.let { toggle(wiFiDetails, it) }
        }
    }

    fun updateGroupBy() {
        val currentGroupBy = MainContext.INSTANCE.settings.groupBy()
        if (currentGroupBy != groupBy) {
            expanded.clear()
            groupBy = currentGroupBy
        }
    }

    fun onGroupCollapsed(wiFiDetails: List<WiFiDetail>, groupPosition: Int) {
        if (!groupBy.none) {
            wiFiDetails.getOrNull(groupPosition)?.let {
                if (it.noChildren) {
                    expanded.remove(groupBy.group(it))
                }
            }
        }
    }

    fun onGroupExpanded(wiFiDetails: List<WiFiDetail>, groupPosition: Int) {
        if (!groupBy.none) {
            wiFiDetails.getOrNull(groupPosition)?.let {
                if (it.noChildren) {
                    expanded.add(groupBy.group(it))
                }
            }
        }
    }

    private fun toggle(wiFiDetails: List<WiFiDetail>, expandableListView: ExpandableListView) =
        (0 until expandableListView.expandableListAdapter.groupCount).forEach {
            val group = groupBy.group(wiFiDetails.getOrNull(it) ?: WiFiDetail.EMPTY)
            if (expanded.contains(group)) {
                expandableListView.expandGroup(it)
            } else {
                expandableListView.collapseGroup(it)
            }
        }

}