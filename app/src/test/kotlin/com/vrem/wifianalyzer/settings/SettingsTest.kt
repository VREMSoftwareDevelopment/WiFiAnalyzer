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
package com.vrem.wifianalyzer.settings

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.nhaarman.mockitokotlin2.*
import com.vrem.util.defaultCountryCode
import com.vrem.util.defaultLanguageTag
import com.vrem.util.ordinals
import com.vrem.util.toLanguageTag
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointViewType
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionViewType
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.graphutils.GraphLegend
import com.vrem.wifianalyzer.wifi.model.GroupBy
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.SortBy
import com.vrem.wifianalyzer.wifi.model.Strength
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import java.util.*


class SettingsTest {
    private val scanSpeedDefault = 5
    private val graphYMultiplier = -10
    private val graphYDefault = 2

    private val repository: Repository = mock()
    private val onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener = mock()
    private val fixture = spy(Settings(repository))

    @Before
    fun setUp() {
        doReturn(false).whenever(fixture).minVersionQ()
        doReturn(true).whenever(fixture).versionP()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
        verifyNoMoreInteractions(onSharedPreferenceChangeListener)
    }

    @Test
    fun testInitializeDefaultValues() {
        fixture.initializeDefaultValues()
        verify(repository).initializeDefaultValues()
    }

    @Test
    fun testRegisterOnSharedPreferenceChangeListener() {
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // validate
        verify(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    @Test
    fun testScanSpeedWithWiFiThrottleDisabled() {
        // setup
        doReturn(true).whenever(fixture).wiFiThrottleDisabled()
        val defaultValue = scanSpeedDefault - 2
        val speedValue = scanSpeedDefault - 1
        whenever(repository.stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)).thenReturn(defaultValue)
        whenever(repository.stringAsInteger(R.string.scan_speed_key, defaultValue)).thenReturn(speedValue)
        // execute
        val actual = fixture.scanSpeed()
        // validate
        assertEquals(scanSpeedDefault, actual)
        verify(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        verify(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
        verify(fixture).wiFiThrottleDisabled()
        verify(fixture).versionP()
    }

    @Test
    fun testScanSpeedWithWiFiThrottleEnabled() {
        // setup
        doReturn(false).whenever(fixture).wiFiThrottleDisabled()
        val defaultValue = scanSpeedDefault - 2
        val speedValue = scanSpeedDefault - 1
        whenever(repository.stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)).thenReturn(defaultValue)
        whenever(repository.stringAsInteger(R.string.scan_speed_key, defaultValue)).thenReturn(speedValue)
        // execute
        val actual = fixture.scanSpeed()
        // validate
        assertEquals(speedValue, actual)
        verify(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        verify(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
        verify(fixture).wiFiThrottleDisabled()
        verify(fixture).versionP()
    }

    @Test
    fun testWiFiThrottleDisabled() {
        // setup
        whenever(repository.resourceBoolean(R.bool.wifi_throttle_disabled_default)).thenReturn(true)
        whenever(repository.boolean(R.string.wifi_throttle_disabled_key, true)).thenReturn(true)
        // execute
        val actual = fixture.wiFiThrottleDisabled()
        // validate
        Assert.assertTrue(actual)
        verify(repository).boolean(R.string.wifi_throttle_disabled_key, true)
        verify(repository).resourceBoolean(R.bool.wifi_throttle_disabled_default)
        verify(fixture).versionP()
    }

    @Test
    fun testGraphMaximumY() {
        // setup
        val defaultValue = 1
        val value = 2
        val expected = value * graphYMultiplier
        whenever(repository.stringAsInteger(R.string.graph_maximum_y_default, graphYDefault)).thenReturn(defaultValue)
        whenever(repository.stringAsInteger(R.string.graph_maximum_y_key, defaultValue)).thenReturn(value)
        // execute
        val actual = fixture.graphMaximumY()
        // validate
        assertEquals(expected, actual)
        verify(repository).stringAsInteger(R.string.graph_maximum_y_default, graphYDefault)
        verify(repository).stringAsInteger(R.string.graph_maximum_y_key, defaultValue)
    }

    @Test
    fun testGroupBy() {
        // setup
        whenever(repository.stringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)).thenReturn(GroupBy.CHANNEL.ordinal)
        // execute
        val actual = fixture.groupBy()
        // validate
        assertEquals(GroupBy.CHANNEL, actual)
        verify(repository).stringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)
    }

    @Test
    fun testSortBy() {
        // setup
        whenever(repository.stringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)).thenReturn(SortBy.SSID.ordinal)
        // execute
        val actual = fixture.sortBy()
        // validate
        assertEquals(SortBy.SSID, actual)
        verify(repository).stringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)
    }

    @Test
    fun testAccessPointView() {
        // setup
        whenever(repository.stringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)).thenReturn(AccessPointViewType.COMPACT.ordinal)
        // execute
        val actual = fixture.accessPointView()
        // validate
        assertEquals(AccessPointViewType.COMPACT, actual)
        verify(repository).stringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)
    }

    @Test
    fun testConnectionViewType() {
        // setup
        whenever(repository.stringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPLETE.ordinal)).thenReturn(ConnectionViewType.COMPACT.ordinal)
        // execute
        val actual = fixture.connectionViewType()
        // validate
        assertEquals(ConnectionViewType.COMPACT, actual)
        verify(repository).stringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPLETE.ordinal)
    }

    @Test
    fun testThemeStyle() {
        // setup
        whenever(repository.stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)).thenReturn(ThemeStyle.LIGHT.ordinal)
        // execute
        val actual = fixture.themeStyle()
        // validate
        assertEquals(ThemeStyle.LIGHT, actual)
        verify(repository).stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
    }

    @Test
    fun testChannelGraphLegend() {
        // setup
        whenever(repository.stringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal)).thenReturn(GraphLegend.RIGHT.ordinal)
        // execute
        val actual = fixture.channelGraphLegend()
        // validate
        assertEquals(GraphLegend.RIGHT, actual)
        verify(repository).stringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal)
    }

    @Test
    fun testTimeGraphLegend() {
        // setup
        whenever(repository.stringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal)).thenReturn(GraphLegend.RIGHT.ordinal)
        // execute
        val actual = fixture.timeGraphLegend()
        // validate
        assertEquals(GraphLegend.RIGHT, actual)
        verify(repository).stringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal)
    }

    @Test
    fun testWiFiBand() {
        // setup
        whenever(repository.stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)).thenReturn(WiFiBand.GHZ5.ordinal)
        // execute
        val actual = fixture.wiFiBand()
        // validate
        assertEquals(WiFiBand.GHZ5, actual)
        verify(repository).stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
    }

    @Test
    fun testFindSSIDs() {
        // setup
        val expected: Set<String> = setOf("value1", "value2", "value3")
        whenever(repository.stringSet(R.string.filter_ssid_key, emptySet())).thenReturn(expected)
        // execute
        val actual = fixture.findSSIDs()
        // validate
        assertEquals(expected, actual)
        verify(repository).stringSet(R.string.filter_ssid_key, emptySet())
    }

    @Test
    fun testSaveSSIDs() {
        // setup
        val values: Set<String> = setOf("value1", "value2", "value3")
        // execute
        fixture.saveSSIDs(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_ssid_key, values)
    }

    @Test
    fun testFindWiFiBands() {
        // setup
        val expected = WiFiBand.GHZ5
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(WiFiBand.values())
        whenever(repository.stringSet(R.string.filter_wifi_band_key, defaultValues)).thenReturn(values)
        // execute
        val actual = fixture.findWiFiBands()
        // validate
        assertEquals(1, actual.size)
        Assert.assertTrue(actual.contains(expected))
        verify(repository).stringSet(R.string.filter_wifi_band_key, defaultValues)
    }

    @Test
    fun testSaveWiFiBands() {
        // setup
        val values = setOf(WiFiBand.GHZ5)
        val expected = setOf("" + WiFiBand.GHZ5.ordinal)
        // execute
        fixture.saveWiFiBands(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_wifi_band_key, expected)
    }

    @Test
    fun testFindStrengths() {
        // setup
        val expected = Strength.THREE
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(Strength.values())
        whenever(repository.stringSet(R.string.filter_strength_key, defaultValues)).thenReturn(values)
        // execute
        val actual = fixture.findStrengths()
        // validate
        assertEquals(1, actual.size)
        Assert.assertTrue(actual.contains(expected))
        verify(repository).stringSet(R.string.filter_strength_key, defaultValues)
    }

    @Test
    fun testSaveStrengths() {
        // setup
        val values = setOf(Strength.TWO)
        val expected = setOf("" + Strength.TWO.ordinal)
        // execute
        fixture.saveStrengths(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_strength_key, expected)
    }

    @Test
    fun testFindSecurities() {
        // setup
        val expected = Security.WPA
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(Security.values())
        whenever(repository.stringSet(R.string.filter_security_key, defaultValues)).thenReturn(values)
        // execute
        val actual = fixture.findSecurities()
        // validate
        assertEquals(1, actual.size)
        Assert.assertTrue(actual.contains(expected))
        verify(repository).stringSet(R.string.filter_security_key, defaultValues)
    }

    @Test
    fun testSaveSecurities() {
        // setup
        val values = setOf(Security.WEP)
        val expected = setOf("" + Security.WEP.ordinal)
        // execute
        fixture.saveSecurities(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_security_key, expected)
    }

    @Test
    fun testToggleWiFiBand() {
        // setup
        whenever(repository.stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)).thenReturn(WiFiBand.GHZ5.ordinal)
        // execute
        fixture.toggleWiFiBand()
        // validate
        verify(repository).stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
        verify(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.toggle().ordinal)
    }

    @Test
    fun testCountryCode() {
        // setup
        val defaultValue = defaultCountryCode()
        val expected = "WW"
        whenever(repository.string(R.string.country_code_key, defaultValue)).thenReturn(expected)
        // execute
        val actual = fixture.countryCode()
        // validate
        assertEquals(expected, actual)
        verify(repository).string(R.string.country_code_key, defaultValue)
    }

    @Test
    fun testLanguageLocale() {
        // setup
        val defaultValue = defaultLanguageTag()
        val expected = Locale.FRENCH
        whenever(repository.string(R.string.language_key, defaultValue)).thenReturn(toLanguageTag(expected))
        // execute
        val actual = fixture.languageLocale()
        // validate
        assertEquals(expected, actual)
        verify(repository).string(R.string.language_key, defaultValue)
    }

    @Test
    fun testSelectedMenu() {
        // setup
        whenever(repository.stringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)).thenReturn(NavigationMenu.CHANNEL_GRAPH.ordinal)
        // execute
        val actual = fixture.selectedMenu()
        // validate
        assertEquals(NavigationMenu.CHANNEL_GRAPH, actual)
        verify(repository).stringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)
    }

    @Test
    fun testSaveSelectedMenu() {
        // execute
        fixture.saveSelectedMenu(NavigationMenu.CHANNEL_GRAPH)
        // validate
        verify(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
    }

    @Test
    fun testSaveSelectedMenuWithNotAllowedMenu() {
        // execute
        fixture.saveSelectedMenu(NavigationMenu.ABOUT)
        // validate
        verify(repository, never()).save(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }

    @Test
    fun testWiFiOffOnExit() {
        // setup
        whenever(repository.resourceBoolean(R.bool.wifi_off_on_exit_default)).thenReturn(true)
        whenever(repository.boolean(R.string.wifi_off_on_exit_key, true)).thenReturn(true)
        // execute
        val actual = fixture.wiFiOffOnExit()
        // validate
        Assert.assertTrue(actual)
        verify(repository).boolean(R.string.wifi_off_on_exit_key, true)
        verify(repository).resourceBoolean(R.bool.wifi_off_on_exit_default)
    }

    @Test
    fun testKeepScreenOn() {
        // setup
        whenever(repository.resourceBoolean(R.bool.keep_screen_on_default)).thenReturn(true)
        whenever(repository.boolean(R.string.keep_screen_on_key, true)).thenReturn(true)
        // execute
        val actual = fixture.keepScreenOn()
        // validate
        Assert.assertTrue(actual)
        verify(repository).boolean(R.string.keep_screen_on_key, true)
        verify(repository).resourceBoolean(R.bool.keep_screen_on_default)
    }
}