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
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*

class AccessPointsAdapterDataTest {
    private val wiFiData: WiFiData = mock()
    private val accessPointsAdapterGroup: AccessPointsAdapterGroup = mock()
    private val expandableListView: ExpandableListView = mock()
    private val settings = INSTANCE.settings
    private val configuration = INSTANCE.configuration
    private val fixture = AccessPointsAdapterData(accessPointsAdapterGroup)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wiFiData)
        verifyNoMoreInteractions(accessPointsAdapterGroup)
        verifyNoMoreInteractions(expandableListView)
        verifyNoMoreInteractions(configuration)
        INSTANCE.restore()
    }

    @Test
    fun beforeUpdate() {
        assertThat(fixture.parentsCount()).isEqualTo(0)
        assertThat(fixture.childrenCount(0)).isEqualTo(0)
        assertThat(fixture.parent(0)).isEqualTo(WiFiDetail.EMPTY)
        assertThat(fixture.parent(-1)).isEqualTo(WiFiDetail.EMPTY)
        assertThat(fixture.child(0, 0)).isEqualTo(WiFiDetail.EMPTY)
        assertThat(fixture.child(0, -1)).isEqualTo(WiFiDetail.EMPTY)
    }

    @Test
    fun afterUpdateWithGroupByChannel() {
        // setup
        val wiFiDetails = withWiFiDetails()
        withSettings()
        whenever(wiFiData.wiFiDetails(any(), eq(SortBy.SSID), eq(GroupBy.CHANNEL))).thenReturn(wiFiDetails)
        // execute
        fixture.update(wiFiData, expandableListView)
        // validate
        verify(wiFiData).wiFiDetails(any(), eq(SortBy.SSID), eq(GroupBy.CHANNEL))
        verify(accessPointsAdapterGroup).update(wiFiDetails, expandableListView)
        verifySettings()
        assertThat(fixture.parentsCount()).isEqualTo(wiFiDetails.size)
        assertThat(fixture.parent(0)).isEqualTo(wiFiDetails[0])
        assertThat(fixture.childrenCount(0)).isEqualTo(wiFiDetails[0].children.size)
        assertThat(fixture.child(0, 0)).isEqualTo(wiFiDetails[0].children[0])
        assertThat(fixture.parent(-1)).isEqualTo(WiFiDetail.EMPTY)
        assertThat(fixture.parent(wiFiDetails.size)).isEqualTo(WiFiDetail.EMPTY)
        assertThat(fixture.child(0, -1)).isEqualTo(WiFiDetail.EMPTY)
        assertThat(fixture.child(0, wiFiDetails[0].children.size)).isEqualTo(WiFiDetail.EMPTY)
    }

    @Test
    fun onGroupCollapsed() {
        // setup
        val index = 11
        val wiFiDetails: List<WiFiDetail> = fixture.wiFiDetails
        // execute
        fixture.onGroupCollapsed(index)
        // validate
        verify(accessPointsAdapterGroup).onGroupCollapsed(wiFiDetails, index)
    }

    @Test
    fun onGroupExpanded() {
        // setup
        val index = 22
        val wiFiDetails: List<WiFiDetail> = fixture.wiFiDetails
        // execute
        fixture.onGroupExpanded(index)
        // validate
        verify(accessPointsAdapterGroup).onGroupExpanded(wiFiDetails, index)
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
        listOf(
            withWiFiDetail(),
            WiFiDetail(WiFiIdentifier("SSID2", "BSSID2")),
            WiFiDetail(WiFiIdentifier("SSID3", "BSSID3"))
        )

    private fun verifySettings() {
        verify(settings).sortBy()
        verify(settings).groupBy()
        verify(settings).findWiFiBands()
        verify(settings).findStrengths()
        verify(settings).findSecurities()
        verify(configuration).size = any()
    }

    private fun withSettings() {
        whenever(settings.sortBy()).thenReturn(SortBy.SSID)
        whenever(settings.groupBy()).thenReturn(GroupBy.CHANNEL)
        whenever(settings.findWiFiBands()).thenReturn(WiFiBand.entries.toSet())
        whenever(settings.findStrengths()).thenReturn(Strength.entries.toSet())
        whenever(settings.findSecurities()).thenReturn(Security.entries.toSet())
    }

}