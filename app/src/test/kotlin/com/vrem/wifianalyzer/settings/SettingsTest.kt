/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
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
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
        verifyNoMoreInteractions(onSharedPreferenceChangeListener)
    }

    @Test
    fun testInitializeDefaultValues() {
        // setup
        doNothing().whenever(repository).initializeDefaultValues()
        // execute
        fixture.initializeDefaultValues()
        // verify
        verify(repository).initializeDefaultValues()
    }

    @Test
    fun testRegisterOnSharedPreferenceChangeListener() {
        // setup
        doNothing().whenever(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // validate
        verify(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    @Test
    fun testScanSpeed() {
        // setup
        val defaultValue = scanSpeedDefault - 2
        val speedValue = scanSpeedDefault - 1
        doReturn(defaultValue).whenever(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        doReturn(speedValue).whenever(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
        // execute
        val actual = fixture.scanSpeed()
        // validate
        assertEquals(speedValue, actual)
        verify(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        verify(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
    }

    @Test
    fun testGraphMaximumY() {
        // setup
        val defaultValue = 1
        val value = 2
        val expected = value * graphYMultiplier
        doReturn(defaultValue).whenever(repository).stringAsInteger(R.string.graph_maximum_y_default, graphYDefault)
        doReturn(value).whenever(repository).stringAsInteger(R.string.graph_maximum_y_key, defaultValue)
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
        doReturn(GroupBy.CHANNEL.ordinal)
            .whenever(repository).stringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)
        // execute
        val actual = fixture.groupBy()
        // validate
        assertEquals(GroupBy.CHANNEL, actual)
        verify(repository).stringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)
    }

    @Test
    fun testSortBy() {
        // setup
        doReturn(SortBy.SSID.ordinal)
            .whenever(repository).stringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)
        // execute
        val actual = fixture.sortBy()
        // validate
        assertEquals(SortBy.SSID, actual)
        verify(repository).stringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)
    }

    @Test
    fun testAccessPointView() {
        // setup
        doReturn(AccessPointViewType.COMPACT.ordinal)
            .whenever(repository).stringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)
        // execute
        val actual = fixture.accessPointView()
        // validate
        assertEquals(AccessPointViewType.COMPACT, actual)
        verify(repository).stringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)
    }

    @Test
    fun testConnectionViewType() {
        // setup
        doReturn(ConnectionViewType.COMPLETE.ordinal)
            .whenever(repository).stringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPACT.ordinal)
        // execute
        val actual = fixture.connectionViewType()
        // validate
        assertEquals(ConnectionViewType.COMPLETE, actual)
        verify(repository).stringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPACT.ordinal)
    }

    @Test
    fun testThemeStyle() {
        // setup
        doReturn(ThemeStyle.LIGHT.ordinal)
            .whenever(repository).stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
        // execute
        val actual = fixture.themeStyle()
        // validate
        assertEquals(ThemeStyle.LIGHT, actual)
        verify(repository).stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
    }

    @Test
    fun testChannelGraphLegend() {
        // setup
        doReturn(GraphLegend.RIGHT.ordinal)
            .whenever(repository).stringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal)
        // execute
        val actual = fixture.channelGraphLegend()
        // validate
        assertEquals(GraphLegend.RIGHT, actual)
        verify(repository).stringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal)
    }

    @Test
    fun testTimeGraphLegend() {
        // setup
        doReturn(GraphLegend.RIGHT.ordinal)
            .whenever(repository).stringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal)
        // execute
        val actual = fixture.timeGraphLegend()
        // validate
        assertEquals(GraphLegend.RIGHT, actual)
        verify(repository).stringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal)
    }

    @Test
    fun testGetWiFiBand() {
        // setup
        doReturn(WiFiBand.GHZ5.ordinal)
            .whenever(repository).stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
        // execute
        val actual = fixture.wiFiBand()
        // validate
        assertEquals(WiFiBand.GHZ5, actual)
        verify(repository).stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
    }

    @Test
    fun testSetWiFiBand() {
        // setup
        doNothing().whenever(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.ordinal)
        // execute
        fixture.wiFiBand(WiFiBand.GHZ5)
        // validate
        verify(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.ordinal)
    }

    @Test
    fun testFindSSIDs() {
        // setup
        val expected: Set<String> = setOf("value1", "value2", "value3")
        doReturn(expected).whenever(repository).stringSet(R.string.filter_ssid_key, setOf())
        // execute
        val actual = fixture.findSSIDs()
        // validate
        assertEquals(expected, actual)
        verify(repository).stringSet(R.string.filter_ssid_key, setOf())
    }

    @Test
    fun testSaveSSIDs() {
        // setup
        val values: Set<String> = setOf("value1", "value2", "value3")
        doNothing().whenever(repository).saveStringSet(R.string.filter_ssid_key, values)
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
        val defaultValues = ordinals(WiFiBand.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_wifi_band_key, defaultValues)
        // execute
        val actual = fixture.findWiFiBands()
        // validate
        assertEquals(1, actual.size)
        assertTrue(actual.contains(expected))
        verify(repository).stringSet(R.string.filter_wifi_band_key, defaultValues)
    }

    @Test
    fun testSaveWiFiBands() {
        // setup
        val values = setOf(WiFiBand.GHZ5)
        val expected = setOf("" + WiFiBand.GHZ5.ordinal)
        doNothing().whenever(repository).saveStringSet(R.string.filter_wifi_band_key, expected)
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
        val defaultValues = ordinals(Strength.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_strength_key, defaultValues)
        // execute
        val actual = fixture.findStrengths()
        // validate
        assertEquals(1, actual.size)
        assertTrue(actual.contains(expected))
        verify(repository).stringSet(R.string.filter_strength_key, defaultValues)
    }

    @Test
    fun testSaveStrengths() {
        // setup
        val values = setOf(Strength.TWO)
        val expected = setOf("" + Strength.TWO.ordinal)
        doNothing().whenever(repository).saveStringSet(R.string.filter_strength_key, expected)
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
        val defaultValues = ordinals(Security.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_security_key, defaultValues)
        // execute
        val actual = fixture.findSecurities()
        // validate
        assertEquals(1, actual.size)
        assertTrue(actual.contains(expected))
        verify(repository).stringSet(R.string.filter_security_key, defaultValues)
    }

    @Test
    fun testSaveSecurities() {
        // setup
        val values = setOf(Security.WEP)
        val expected = setOf("" + Security.WEP.ordinal)
        doNothing().whenever(repository).saveStringSet(R.string.filter_security_key, expected)
        // execute
        fixture.saveSecurities(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_security_key, expected)
    }

    @Test
    fun testCountryCode() {
        // setup
        val defaultValue = defaultCountryCode()
        val expected = "WW"
        doReturn(expected).whenever(repository).string(R.string.country_code_key, defaultValue)
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
        doReturn(toLanguageTag(expected)).whenever(repository).string(R.string.language_key, defaultValue)
        // execute
        val actual = fixture.languageLocale()
        // validate
        assertEquals(expected, actual)
        verify(repository).string(R.string.language_key, defaultValue)
    }

    @Test
    fun testSelectedMenu() {
        // setup
        doReturn(NavigationMenu.CHANNEL_GRAPH.ordinal)
            .whenever(repository).stringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)
        // execute
        val actual = fixture.selectedMenu()
        // validate
        assertEquals(NavigationMenu.CHANNEL_GRAPH, actual)
        verify(repository).stringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)
    }

    @Test
    fun testSaveSelectedMenu() {
        // setup
        doNothing().whenever(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
        // execute
        fixture.saveSelectedMenu(NavigationMenu.CHANNEL_GRAPH)
        // validate
        verify(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
    }

    @Test
    fun testSaveSelectedMenuWithNotAllowedMenu() {
        // execute
        fixture.saveSelectedMenu(NavigationMenu.ABOUT)
    }

    @Test
    fun testWiFiOffOnExit() {
        // setup
        doReturn(true).whenever(repository).resourceBoolean(R.bool.wifi_off_on_exit_default)
        doReturn(true).whenever(repository).boolean(R.string.wifi_off_on_exit_key, true)
        // execute
        val actual = fixture.wiFiOffOnExit()
        // validate
        assertTrue(actual)
        verify(repository).boolean(R.string.wifi_off_on_exit_key, true)
        verify(repository).resourceBoolean(R.bool.wifi_off_on_exit_default)
    }

    @Test
    fun testKeepScreenOn() {
        // setup
        doReturn(true).whenever(repository).resourceBoolean(R.bool.keep_screen_on_default)
        doReturn(true).whenever(repository).boolean(R.string.keep_screen_on_key, true)
        // execute
        val actual = fixture.keepScreenOn()
        // validate
        assertTrue(actual)
        verify(repository).boolean(R.string.keep_screen_on_key, true)
        verify(repository).resourceBoolean(R.bool.keep_screen_on_default)
    }

    @Test
    fun testCacheOff() {
        // setup
        doReturn(true).whenever(repository).resourceBoolean(R.bool.cache_off_default)
        doReturn(true).whenever(repository).boolean(R.string.cache_off_key, true)
        // execute
        val actual = fixture.cacheOff()
        // validate
        assertTrue(actual)
        verify(repository).boolean(R.string.cache_off_key, true)
        verify(repository).resourceBoolean(R.bool.cache_off_default)
    }

}