/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.EnumUtils
import com.vrem.util.defaultCountryCode
import com.vrem.util.defaultLanguageTag
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
import org.mockito.Mockito.*
import java.util.*


class SettingsTest {
    private val repository = mock(Repository::class.java)
    private val onSharedPreferenceChangeListener = mock(OnSharedPreferenceChangeListener::class.java)
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
    fun testGetScanSpeedWithWiFiThrottleDisabled() {
        // setup
        doReturn(true).whenever(fixture).isWiFiThrottleDisabled()
        val defaultValue = Settings.SCAN_SPEED_DEFAULT - 2
        val speedValue = Settings.SCAN_SPEED_DEFAULT - 1
        whenever(repository.getStringAsInteger(R.string.scan_speed_default, Settings.SCAN_SPEED_DEFAULT)).thenReturn(defaultValue)
        whenever(repository.getStringAsInteger(R.string.scan_speed_key, defaultValue)).thenReturn(speedValue)
        // execute
        val actual = fixture.getScanSpeed()
        // validate
        assertEquals(Settings.SCAN_SPEED_DEFAULT, actual)
        verify(repository).getStringAsInteger(R.string.scan_speed_default, Settings.SCAN_SPEED_DEFAULT)
        verify(repository).getStringAsInteger(R.string.scan_speed_key, defaultValue)
        verify(fixture).isWiFiThrottleDisabled()
        verify(fixture).versionP()
    }

    @Test
    fun testGetScanSpeedWithWiFiThrottleEnabled() {
        // setup
        doReturn(false).whenever(fixture).isWiFiThrottleDisabled()
        val defaultValue = Settings.SCAN_SPEED_DEFAULT - 2
        val speedValue = Settings.SCAN_SPEED_DEFAULT - 1
        whenever(repository.getStringAsInteger(R.string.scan_speed_default, Settings.SCAN_SPEED_DEFAULT)).thenReturn(defaultValue)
        whenever(repository.getStringAsInteger(R.string.scan_speed_key, defaultValue)).thenReturn(speedValue)
        // execute
        val actual = fixture.getScanSpeed()
        // validate
        assertEquals(speedValue, actual)
        verify(repository).getStringAsInteger(R.string.scan_speed_default, Settings.SCAN_SPEED_DEFAULT)
        verify(repository).getStringAsInteger(R.string.scan_speed_key, defaultValue)
        verify(fixture).isWiFiThrottleDisabled()
        verify(fixture).versionP()
    }

    @Test
    fun testIsWiFiThrottleDisabled() {
        // setup
        whenever(repository.getResourceBoolean(R.bool.wifi_throttle_disabled_default)).thenReturn(true)
        whenever(repository.getBoolean(R.string.wifi_throttle_disabled_key, true)).thenReturn(true)
        // execute
        val actual = fixture.isWiFiThrottleDisabled()
        // validate
        Assert.assertTrue(actual)
        verify(repository).getBoolean(R.string.wifi_throttle_disabled_key, true)
        verify(repository).getResourceBoolean(R.bool.wifi_throttle_disabled_default)
        verify(fixture).versionP()
    }

    @Test
    fun testGetGraphMaximumY() {
        // setup
        val defaultValue = 1
        val value = 2
        val expected = value * Settings.GRAPH_Y_MULTIPLIER
        whenever(repository.getStringAsInteger(R.string.graph_maximum_y_default, Settings.GRAPH_Y_DEFAULT)).thenReturn(defaultValue)
        whenever(repository.getStringAsInteger(R.string.graph_maximum_y_key, defaultValue)).thenReturn(value)
        // execute
        val actual = fixture.getGraphMaximumY()
        // validate
        assertEquals(expected, actual)
        verify(repository).getStringAsInteger(R.string.graph_maximum_y_default, Settings.GRAPH_Y_DEFAULT)
        verify(repository).getStringAsInteger(R.string.graph_maximum_y_key, defaultValue)
    }

    @Test
    fun testGetGroupBy() {
        // setup
        whenever(repository.getStringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)).thenReturn(GroupBy.CHANNEL.ordinal)
        // execute
        val actual = fixture.getGroupBy()
        // validate
        assertEquals(GroupBy.CHANNEL, actual)
        verify(repository).getStringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)
    }

    @Test
    fun testGetSortBy() {
        // setup
        whenever(repository.getStringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)).thenReturn(SortBy.SSID.ordinal)
        // execute
        val actual = fixture.getSortBy()
        // validate
        assertEquals(SortBy.SSID, actual)
        verify(repository).getStringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)
    }

    @Test
    fun testGetAccessPointView() {
        // setup
        whenever(repository.getStringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)).thenReturn(AccessPointViewType.COMPACT.ordinal)
        // execute
        val actual = fixture.getAccessPointView()
        // validate
        assertEquals(AccessPointViewType.COMPACT, actual)
        verify(repository).getStringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)
    }

    @Test
    fun testGetConnectionViewType() {
        // setup
        whenever(repository.getStringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPLETE.ordinal)).thenReturn(ConnectionViewType.COMPACT.ordinal)
        // execute
        val actual = fixture.getConnectionViewType()
        // validate
        assertEquals(ConnectionViewType.COMPACT, actual)
        verify(repository).getStringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPLETE.ordinal)
    }

    @Test
    fun testGetThemeStyle() {
        // setup
        whenever(repository.getStringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)).thenReturn(ThemeStyle.LIGHT.ordinal)
        // execute
        val actual = fixture.getThemeStyle()
        // validate
        assertEquals(ThemeStyle.LIGHT, actual)
        verify(repository).getStringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
    }

    @Test
    fun testGetChannelGraphLegend() {
        // setup
        whenever(repository.getStringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal)).thenReturn(GraphLegend.RIGHT.ordinal)
        // execute
        val actual = fixture.getChannelGraphLegend()
        // validate
        assertEquals(GraphLegend.RIGHT, actual)
        verify(repository).getStringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal)
    }

    @Test
    fun testGetTimeGraphLegend() {
        // setup
        whenever(repository.getStringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal)).thenReturn(GraphLegend.RIGHT.ordinal)
        // execute
        val actual = fixture.getTimeGraphLegend()
        // validate
        assertEquals(GraphLegend.RIGHT, actual)
        verify(repository).getStringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal)
    }

    @Test
    fun testGetWiFiBand() {
        // setup
        whenever(repository.getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)).thenReturn(WiFiBand.GHZ5.ordinal)
        // execute
        val actual = fixture.getWiFiBand()
        // validate
        assertEquals(WiFiBand.GHZ5, actual)
        verify(repository).getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
    }

    @Test
    fun testGetSSIDFilter() {
        // setup
        val expected: Set<String> = setOf("value1", "value2", "value3")
        whenever(repository.getStringSet(R.string.filter_ssid_key, emptySet())).thenReturn(expected)
        // execute
        val actual = fixture.getSSIDs()
        // validate
        assertEquals(expected, actual)
        verify(repository).getStringSet(R.string.filter_ssid_key, emptySet())
    }

    @Test
    fun testSaveSSIDFilter() {
        // setup
        val values: Set<String> = setOf("value1", "value2", "value3")
        // execute
        fixture.saveSSIDs(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_ssid_key, values)
    }

    @Test
    fun testGetWiFiBandFilter() {
        // setup
        val expected = WiFiBand.GHZ5
        val values = setOf("" + expected.ordinal)
        val defaultValues = EnumUtils.ordinals(WiFiBand::class.java)
        whenever(repository.getStringSet(R.string.filter_wifi_band_key, defaultValues)).thenReturn(values)
        // execute
        val actual = fixture.getWiFiBands()
        // validate
        assertEquals(1, actual.size)
        Assert.assertTrue(actual.contains(expected))
        verify(repository).getStringSet(R.string.filter_wifi_band_key, defaultValues)
    }

    @Test
    fun testSaveWiFiBandFilter() {
        // setup
        val values = setOf(WiFiBand.GHZ5)
        val expected = setOf("" + WiFiBand.GHZ5.ordinal)
        // execute
        fixture.saveWiFiBands(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_wifi_band_key, expected)
    }

    @Test
    fun testGetStrengthFilter() {
        // setup
        val expected = Strength.THREE
        val values = setOf("" + expected.ordinal)
        val defaultValues = EnumUtils.ordinals(Strength::class.java)
        whenever(repository.getStringSet(R.string.filter_strength_key, defaultValues)).thenReturn(values)
        // execute
        val actual = fixture.getStrengths()
        // validate
        assertEquals(1, actual.size)
        Assert.assertTrue(actual.contains(expected))
        verify(repository).getStringSet(R.string.filter_strength_key, defaultValues)
    }

    @Test
    fun testSaveStrengthFilter() {
        // setup
        val values = setOf(Strength.TWO)
        val expected = setOf("" + Strength.TWO.ordinal)
        // execute
        fixture.saveStrengths(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_strength_key, expected)
    }

    @Test
    fun testGetSecurityFilter() {
        // setup
        val expected = Security.WPA
        val values = setOf("" + expected.ordinal)
        val defaultValues = EnumUtils.ordinals(Security::class.java)
        whenever(repository.getStringSet(R.string.filter_security_key, defaultValues)).thenReturn(values)
        // execute
        val actual = fixture.getSecurities()
        // validate
        assertEquals(1, actual.size)
        Assert.assertTrue(actual.contains(expected))
        verify(repository).getStringSet(R.string.filter_security_key, defaultValues)
    }

    @Test
    fun testSaveSecurityFilter() {
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
        whenever(repository.getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)).thenReturn(WiFiBand.GHZ5.ordinal)
        // execute
        fixture.toggleWiFiBand()
        // validate
        verify(repository).getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
        verify(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.toggle().ordinal)
    }

    @Test
    fun testGetCountryCode() {
        // setup
        val defaultValue = defaultCountryCode()
        val expected = "WW"
        whenever(repository.getString(R.string.country_code_key, defaultValue)).thenReturn(expected)
        // execute
        val actual = fixture.getCountryCode()
        // validate
        assertEquals(expected, actual)
        verify(repository).getString(R.string.country_code_key, defaultValue)
    }

    @Test
    fun testGetLanguageLocale() {
        // setup
        val defaultValue = defaultLanguageTag()
        val expected = Locale.FRENCH
        whenever(repository.getString(R.string.language_key, defaultValue)).thenReturn(toLanguageTag(expected))
        // execute
        val actual = fixture.getLanguageLocale()
        // validate
        assertEquals(expected, actual)
        verify(repository).getString(R.string.language_key, defaultValue)
    }

    @Test
    fun testGetSelectedMenu() {
        // setup
        whenever(repository.getStringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)).thenReturn(NavigationMenu.CHANNEL_GRAPH.ordinal)
        // execute
        val actual = fixture.getSelectedMenu()
        // validate
        assertEquals(NavigationMenu.CHANNEL_GRAPH, actual)
        verify(repository).getStringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)
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
    fun testIsWiFiOffOnExit() {
        // setup
        whenever(repository.getResourceBoolean(R.bool.wifi_off_on_exit_default)).thenReturn(true)
        whenever(repository.getBoolean(R.string.wifi_off_on_exit_key, true)).thenReturn(true)
        // execute
        val actual = fixture.isWiFiOffOnExit()
        // validate
        Assert.assertTrue(actual)
        verify(repository).getBoolean(R.string.wifi_off_on_exit_key, true)
        verify(repository).getResourceBoolean(R.bool.wifi_off_on_exit_default)
    }

    @Test
    fun testIsKeepScreenOn() {
        // setup
        whenever(repository.getResourceBoolean(R.bool.keep_screen_on_default)).thenReturn(true)
        whenever(repository.getBoolean(R.string.keep_screen_on_key, true)).thenReturn(true)
        // execute
        val actual = fixture.isKeepScreenOn()
        // validate
        Assert.assertTrue(actual)
        verify(repository).getBoolean(R.string.keep_screen_on_key, true)
        verify(repository).getResourceBoolean(R.bool.keep_screen_on_default)
    }
}