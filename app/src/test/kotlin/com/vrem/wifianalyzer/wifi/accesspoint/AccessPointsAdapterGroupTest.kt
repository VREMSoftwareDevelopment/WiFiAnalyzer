/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.wifi.model.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

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
    fun testBeforeUpdate() {
        assertEquals(GroupBy.NONE, fixture.groupBy)
        assertTrue(fixture.expanded.isEmpty())
    }

    @Test
    fun testAfterUpdateWithGroupByChannel() {
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
        assertEquals(GroupBy.CHANNEL, fixture.groupBy)
    }

    @Test
    fun testUpdateGroupBy() {
        // setup
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
        // execute
        fixture.updateGroupBy()
        // validate
        verify(settings).groupBy()
        assertEquals(GroupBy.SSID, fixture.groupBy)
    }

    @Test
    fun testUpdateGroupByWillClearExpandedWhenGroupByIsChanged() {
        // setup
        fixture.expanded.add("TEST")
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
        // execute
        fixture.updateGroupBy()
        // validate
        verify(settings).groupBy()
        assertEquals(GroupBy.SSID, fixture.groupBy)
        assertTrue(fixture.expanded.isEmpty())
    }

    @Test
    fun testUpdateGroupByWillNotClearExpandedWhenGroupByIsSame() {
        // setup
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
        fixture.updateGroupBy()
        fixture.expanded.add("TEST")
        // execute
        fixture.updateGroupBy()
        // validate
        assertFalse(fixture.expanded.isEmpty())
    }

    @Test
    fun testOnGroupExpanded() {
        // setup
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
        fixture.updateGroupBy()
        val wiFiDetails = withWiFiDetails()
        // execute
        fixture.onGroupExpanded(wiFiDetails, 0)
        // validate
        assertTrue(fixture.expanded.contains(wiFiDetails[0].wiFiIdentifier.ssid))
    }

    @Test
    fun testOnGroupCollapsed() {
        // setup
        whenever(settings.groupBy()).thenReturn(GroupBy.SSID)
        fixture.updateGroupBy()
        val wiFiDetails = withWiFiDetails()
        fixture.onGroupExpanded(wiFiDetails, 0)
        // execute
        fixture.onGroupCollapsed(wiFiDetails, 0)
        // validate
        assertTrue(fixture.expanded.isEmpty())
    }

    private fun withWiFiDetail(): WiFiDetail =
            WiFiDetail(WiFiIdentifier("SSID1", "BSSID1"),
                    wiFiSignal = WiFiSignal(2255, 2255, WiFiWidth.MHZ_20, -40, true),
                    children = listOf(
                            WiFiDetail(WiFiIdentifier("SSID1-1", "BSSID1-1")),
                            WiFiDetail(WiFiIdentifier("SSID1-2", "BSSID1-2")),
                            WiFiDetail(WiFiIdentifier("SSID1-3", "BSSID1-3"))))

    private fun withWiFiDetails(): List<WiFiDetail> =
            listOf(withWiFiDetail(), WiFiDetail(WiFiIdentifier("SSID2", "BSSID2")), WiFiDetail(WiFiIdentifier("SSID3", "BSSID3")))
}