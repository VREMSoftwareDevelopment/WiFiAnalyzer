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

import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*

class AccessPointsAdapterGroupTest {
    private val expandableListView: ExpandableListView = mock()
    private val expandableListAdapter: ExpandableListAdapter = mock()
    private val settings = INSTANCE.settings
    private val fixture = AccessPointsAdapterGroup()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(expandableListView)
        verifyNoMoreInteractions(expandableListAdapter)
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
        whenever(settings.groupBy()).thenReturn(GroupBy.CHANNEL)
        whenever(expandableListView.expandableListAdapter).thenReturn(expandableListAdapter)
        whenever(expandableListAdapter.groupCount).thenReturn(wiFiDetails.size)
        // execute
        fixture.update(wiFiDetails, expandableListView)
        // validate
        verify(settings).groupBy()
        verify(expandableListView).expandableListAdapter
        verify(expandableListAdapter).groupCount
        verify(expandableListView, times(3)).collapseGroup(any())
        assertThat(fixture.groupBy).isEqualTo(GroupBy.CHANNEL)
    }

    @Test
    fun updateGroupBy() {
        // setup
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
        // execute
        fixture.updateGroupBy()
        // validate
        verify(settings).groupBy()
        assertThat(fixture.groupBy).isEqualTo(GroupBy.SSID)
    }

    @Test
    fun updateGroupByWillClearExpandedWhenGroupByIsChanged() {
        // setup
        fixture.expanded.add("TEST")
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
        // execute
        fixture.updateGroupBy()
        // validate
        verify(settings).groupBy()
        assertThat(fixture.groupBy).isEqualTo(GroupBy.SSID)
        assertThat(fixture.expanded).isEmpty()
    }

    @Test
    fun updateGroupByWillNotClearExpandedWhenGroupByIsSame() {
        // setup
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
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
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
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
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
        fixture.updateGroupBy()
        val wiFiDetails = withWiFiDetails()
        fixture.onGroupExpanded(wiFiDetails, 0)
        // execute
        fixture.onGroupCollapsed(wiFiDetails, 0)
        // validate
        assertThat(fixture.expanded).isEmpty()
    }

    private fun withWiFiDetail(): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier("SSID1", "BSSID1"),
            wiFiSignal = WiFiSignal(2255, 2255, WiFiWidth.MHZ_20, -40),
            children = listOf(
                WiFiDetail(WiFiIdentifier("SSID1-1", "BSSID1-1")),
                WiFiDetail(WiFiIdentifier("SSID1-2", "BSSID1-2")),
                WiFiDetail(WiFiIdentifier("SSID1-3", "BSSID1-3"))
            )
        )

    private fun withWiFiDetails(): List<WiFiDetail> =
        listOf(withWiFiDetail(), WiFiDetail(WiFiIdentifier("SSID2", "BSSID2")), WiFiDetail(WiFiIdentifier("SSID3", "BSSID3")))
}