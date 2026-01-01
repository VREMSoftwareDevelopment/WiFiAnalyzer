/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.wifi.model.GroupBy
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class AccessPointsAdapterGroupTest {
    private val expandableListView: ExpandableListView = mock()
    private val expandableListAdapter: ExpandableListAdapter = mock()
    private val wiFiDetailWithChildren = mock<WiFiDetail>()
    private val settings = INSTANCE.settings
    private val fixture = AccessPointsAdapterGroup()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(expandableListView)
        verifyNoMoreInteractions(expandableListAdapter)
        verifyNoMoreInteractions(wiFiDetailWithChildren)
        INSTANCE.restore()
    }

    @Test
    fun beforeUpdate() {
        assertThat(fixture.groupBy).isEqualTo(GroupBy.NONE)
        assertThat(fixture.expanded).isEmpty()
    }

    @Test
    fun afterUpdateWithGroupByChannel() {
        // setup
        val wiFiDetails = withWiFiDetails()
        doReturn(GroupBy.CHANNEL).whenever(settings).groupBy()
        doReturn(expandableListAdapter).whenever(expandableListView).expandableListAdapter
        doReturn(wiFiDetails.size).whenever(expandableListAdapter).groupCount
        // execute
        fixture.update(wiFiDetails, expandableListView)
        // validate
        assertThat(fixture.groupBy).isEqualTo(GroupBy.CHANNEL)
        verify(settings).groupBy()
        verify(expandableListView).expandableListAdapter
        verify(expandableListAdapter).groupCount
        verify(expandableListView, times(3)).collapseGroup(any())
    }

    @Test
    fun updateGroupBy() {
        // setup
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        // execute
        fixture.updateGroupBy()
        // validate
        assertThat(fixture.groupBy).isEqualTo(GroupBy.SSID)
        verify(settings).groupBy()
    }

    @Test
    fun updateGroupByWillClearExpandedWhenGroupByIsChanged() {
        // setup
        fixture.expanded.add("TEST")
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        // execute
        fixture.updateGroupBy()
        // validate
        assertThat(fixture.groupBy).isEqualTo(GroupBy.SSID)
        assertThat(fixture.expanded).isEmpty()
        verify(settings).groupBy()
    }

    @Test
    fun updateGroupByWillNotClearExpandedWhenGroupByIsSame() {
        // setup
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        fixture.updateGroupBy()
        fixture.expanded.add("TEST")
        // execute
        fixture.updateGroupBy()
        // validate
        assertThat(fixture.expanded).isNotEmpty()
    }

    @Test
    fun onGroupExpanded() {
        // setup
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        fixture.updateGroupBy()
        val wiFiDetails = withWiFiDetails()
        // execute
        fixture.onGroupExpanded(wiFiDetails, 0)
        // validate
        assertThat(fixture.expanded).contains(wiFiDetails[0].wiFiIdentifier.ssid)
    }

    @Test
    fun onGroupCollapsed() {
        // setup
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        fixture.updateGroupBy()
        val wiFiDetails = withWiFiDetails()
        fixture.onGroupExpanded(wiFiDetails, 0)
        // execute
        fixture.onGroupCollapsed(wiFiDetails, 0)
        // validate
        assertThat(fixture.expanded).isEmpty()
    }

    @Test
    fun updateWithGroupByNoneDoesNotInteractWithExpandableListView() {
        // setup
        val wiFiDetails = withWiFiDetails()
        doReturn(GroupBy.NONE).whenever(settings).groupBy()
        // execute
        fixture.update(wiFiDetails, expandableListView)
        // validate
        verify(settings).groupBy()
    }

    @Test
    fun updateWithExpandableListViewNullDoesNotThrow() {
        // setup
        val wiFiDetails = withWiFiDetails()
        doReturn(GroupBy.CHANNEL).whenever(settings).groupBy()
        // execute
        fixture.update(wiFiDetails, null)
        // validate
        verify(settings).groupBy()
    }

    @Test
    fun onGroupExpandedWithGroupByNoneDoesNothing() {
        // setup
        val wiFiDetails = withWiFiDetails()
        doReturn(GroupBy.NONE).whenever(settings).groupBy()
        fixture.updateGroupBy()
        // execute
        fixture.onGroupExpanded(wiFiDetails, 0)
        // validate
        assertThat(fixture.expanded).isEmpty()
    }

    @Test
    fun onGroupCollapsedWithGroupByNoneDoesNothing() {
        // setup
        val wiFiDetails = withWiFiDetails()
        doReturn(GroupBy.NONE).whenever(settings).groupBy()
        fixture.updateGroupBy()
        fixture.expanded.add("test")
        // execute
        fixture.onGroupCollapsed(wiFiDetails, 0)
        // validate
        assertThat(fixture.expanded).contains("test")
    }

    @Test
    fun onGroupExpandedWithInvalidGroupPositionDoesNothing() {
        // setup
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        fixture.updateGroupBy()
        val wiFiDetails = withWiFiDetails()
        // execute
        fixture.onGroupExpanded(wiFiDetails, -1)
        fixture.onGroupExpanded(wiFiDetails, wiFiDetails.size)
        // validate
        assertThat(fixture.expanded).isEmpty()
    }

    @Test
    fun onGroupCollapsedWithInvalidGroupPositionDoesNothing() {
        // setup
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        fixture.updateGroupBy()
        val wiFiDetails = withWiFiDetails()
        fixture.expanded.add("test")
        // execute
        fixture.onGroupCollapsed(wiFiDetails, -1)
        fixture.onGroupCollapsed(wiFiDetails, wiFiDetails.size)
        // validate
        assertThat(fixture.expanded).contains("test")
    }

    @Test
    fun updateWithEmptyWiFiDetailsDoesNotThrow() {
        // setup
        doReturn(GroupBy.CHANNEL).whenever(settings).groupBy()
        doReturn(expandableListAdapter).whenever(expandableListView).expandableListAdapter
        doReturn(0).whenever(expandableListAdapter).groupCount
        // execute
        fixture.update(emptyList(), expandableListView)
        // validate
        verify(settings).groupBy()
        verify(expandableListView).expandableListAdapter
        verify(expandableListAdapter).groupCount
    }

    @Test
    fun onGroupCollapsedDoesNotRemoveIfHasChildren() {
        // setup
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        fixture.updateGroupBy()
        doReturn(false).whenever(wiFiDetailWithChildren).noChildren
        val wiFiDetails = listOf(wiFiDetailWithChildren)
        fixture.expanded.add("test")
        // execute
        fixture.onGroupCollapsed(wiFiDetails, 0)
        // validate
        assertThat(fixture.expanded).contains("test")
        verify(wiFiDetailWithChildren).noChildren
    }

    @Test
    fun onGroupExpandedDoesNotAddIfHasChildren() {
        // setup
        doReturn(GroupBy.SSID).whenever(settings).groupBy()
        fixture.updateGroupBy()
        doReturn(false).whenever(wiFiDetailWithChildren).noChildren
        val wiFiDetails = listOf(wiFiDetailWithChildren)
        // execute
        fixture.onGroupExpanded(wiFiDetails, 0)
        // validate
        assertThat(fixture.expanded).isEmpty()
        verify(wiFiDetailWithChildren).noChildren
    }

    private fun withWiFiDetail(): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier("SSID1", "BSSID1"),
            wiFiSignal = WiFiSignal(2255, 2255, WiFiWidth.MHZ_20, -40),
            children =
                listOf(
                    WiFiDetail(WiFiIdentifier("SSID1-1", "BSSID1-1")),
                    WiFiDetail(WiFiIdentifier("SSID1-2", "BSSID1-2")),
                    WiFiDetail(WiFiIdentifier("SSID1-3", "BSSID1-3")),
                ),
        )

    private fun withWiFiDetails(): List<WiFiDetail> =
        listOf(
            withWiFiDetail(),
            WiFiDetail(WiFiIdentifier("SSID2", "BSSID2")),
            WiFiDetail(WiFiIdentifier("SSID3", "BSSID3")),
        )
}
