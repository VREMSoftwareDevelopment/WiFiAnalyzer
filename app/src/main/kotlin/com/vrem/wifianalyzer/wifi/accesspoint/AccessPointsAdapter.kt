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

import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier

@OpenClass
class AccessPointsAdapter(
    private val accessPointsAdapterData: AccessPointsAdapterData = AccessPointsAdapterData(),
    private val accessPointDetail: AccessPointDetail = AccessPointDetail(),
    private val accessPointPopup: AccessPointPopup = AccessPointPopup()
) : BaseExpandableListAdapter(), UpdateNotifier {

    lateinit var expandableListView: ExpandableListView

    override fun getGroupView(groupPosition: Int, expanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val wiFiDetail = getGroup(groupPosition)
        val view = accessPointDetail.makeView(convertView, parent, wiFiDetail)
        attachPopup(view, wiFiDetail)
        val groupIndicator = view.findViewById<ImageView>(R.id.groupIndicator)
        val childrenCount = getChildrenCount(groupPosition)
        if (childrenCount > 0) {
            groupIndicator.visibility = View.VISIBLE
            groupIndicator.setImageResource(if (expanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more)
        } else {
            groupIndicator.visibility = View.GONE
        }
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, lastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val wiFiDetail = getChild(groupPosition, childPosition)
        val view = accessPointDetail.makeView(convertView, parent, wiFiDetail, true)
        attachPopup(view, wiFiDetail)
        view.findViewById<View>(R.id.groupIndicator).visibility = View.GONE
        return view
    }

    override fun update(wiFiData: WiFiData) {
        accessPointsAdapterData.update(wiFiData, expandableListView)
        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int =
        accessPointsAdapterData.parentsCount()

    override fun getChildrenCount(groupPosition: Int): Int =
        accessPointsAdapterData.childrenCount(groupPosition)

    override fun getGroup(groupPosition: Int): WiFiDetail =
        accessPointsAdapterData.parent(groupPosition)

    override fun getChild(groupPosition: Int, childPosition: Int): WiFiDetail =
        accessPointsAdapterData.child(groupPosition, childPosition)

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = true

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun onGroupCollapsed(groupPosition: Int) =
        accessPointsAdapterData.onGroupCollapsed(groupPosition)

    override fun onGroupExpanded(groupPosition: Int) =
        accessPointsAdapterData.onGroupExpanded(groupPosition)

    private fun attachPopup(view: View, wiFiDetail: WiFiDetail) {
        view.findViewById<View>(R.id.attachPopup)?.let {
            accessPointPopup.attach(it, wiFiDetail)
            accessPointPopup.attach(view.findViewById(R.id.ssid), wiFiDetail)
        }
    }

}