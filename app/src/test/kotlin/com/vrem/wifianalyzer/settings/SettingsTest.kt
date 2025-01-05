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
package com.vrem.wifianalyzer.settings

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class SettingsTest {
    private val scanSpeedDefault = 5
    private val graphYMultiplier = -10
    private val graphYDefault = 2

    private val repository: Repository = mock()
    private val onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener = mock()
    private val fixture: Settings = Settings(repository)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
        verifyNoMoreInteractions(onSharedPreferenceChangeListener)
    }

    @Test
    fun initializeDefaultValues() {
        // setup
        doNothing().whenever(repository).initializeDefaultValues()
        // execute
        fixture.initializeDefaultValues()
        // verify
        verify(repository).initializeDefaultValues()
    }

    @Test
    fun registerOnSharedPreferenceChangeListener() {
        // setup
        doNothing().whenever(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // validate
        verify(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    @Test
    fun scanSpeed() {
        // setup
        val defaultValue = scanSpeedDefault - 2
        val speedValue = scanSpeedDefault - 1
        doReturn(defaultValue).whenever(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        doReturn(speedValue).whenever(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
        // execute
        val actual = fixture.scanSpeed()
        // validate
        assertThat(actual).isEqualTo(speedValue)
        verify(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        verify(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
    }

    @Test
    fun graphMaximumY() {
        // setup
        val defaultValue = 1
        val value = 2
        val expected = value * graphYMultiplier
        doReturn(defaultValue).whenever(repository).stringAsInteger(R.string.graph_maximum_y_default, graphYDefault)
        doReturn(value).whenever(repository).stringAsInteger(R.string.graph_maximum_y_key, defaultValue)
        // execute
        val actual = fixture.graphMaximumY()
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(repository).stringAsInteger(R.string.graph_maximum_y_default, graphYDefault)
        verify(repository).stringAsInteger(R.string.graph_maximum_y_key, defaultValue)
    }

    @Test
    fun groupBy() {
        // setup
        doReturn(GroupBy.CHANNEL.ordinal)
            .whenever(repository).stringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)
        // execute
        val actual = fixture.groupBy()
        // validate
        assertThat(actual).isEqualTo(GroupBy.CHANNEL)
        verify(repository).stringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)
    }

    @Test
    fun sortBy() {
        // setup
        doReturn(SortBy.SSID.ordinal)
            .whenever(repository).stringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)
        // execute
        val actual = fixture.sortBy()
        // validate
        assertThat(actual).isEqualTo(SortBy.SSID)
        verify(repository).stringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)
    }

    @Test
    fun accessPointView() {
        // setup
        doReturn(AccessPointViewType.COMPACT.ordinal)
            .whenever(repository).stringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)
        // execute
        val actual = fixture.accessPointView()
        // validate
        assertThat(actual).isEqualTo(AccessPointViewType.COMPACT)
        verify(repository).stringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)
    }

    @Test
    fun connectionViewType() {
        // setup
        doReturn(ConnectionViewType.COMPLETE.ordinal)
            .whenever(repository).stringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPACT.ordinal)
        // execute
        val actual = fixture.connectionViewType()
        // validate
        assertThat(actual).isEqualTo(ConnectionViewType.COMPLETE)
        verify(repository).stringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPACT.ordinal)
    }

    @Test
    fun themeStyle() {
        // setup
        doReturn(ThemeStyle.LIGHT.ordinal)
            .whenever(repository).stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
        // execute
        val actual = fixture.themeStyle()
        // validate
        assertThat(actual).isEqualTo(ThemeStyle.LIGHT)
        verify(repository).stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
    }

    @Test
    fun channelGraphLegend() {
        // setup
        doReturn(GraphLegend.RIGHT.ordinal)
            .whenever(repository).stringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal)
        // execute
        val actual = fixture.channelGraphLegend()
        // validate
        assertThat(actual).isEqualTo(GraphLegend.RIGHT)
        verify(repository).stringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal)
    }

    @Test
    fun timeGraphLegend() {
        // setup
        doReturn(GraphLegend.RIGHT.ordinal)
            .whenever(repository).stringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal)
        // execute
        val actual = fixture.timeGraphLegend()
        // validate
        assertThat(actual).isEqualTo(GraphLegend.RIGHT)
        verify(repository).stringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal)
    }

    @Test
    fun getWiFiBand() {
        // setup
        doReturn(WiFiBand.GHZ5.ordinal)
            .whenever(repository).stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
        // execute
        val actual = fixture.wiFiBand()
        // validate
        assertThat(actual).isEqualTo(WiFiBand.GHZ5)
        verify(repository).stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
    }

    @Test
    fun setWiFiBand() {
        // setup
        doNothing().whenever(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.ordinal)
        // execute
        fixture.wiFiBand(WiFiBand.GHZ5)
        // validate
        verify(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.ordinal)
    }

    @Test
    fun findSSIDs() {
        // setup
        val expected: Set<String> = setOf("value1", "value2", "value3")
        doReturn(expected).whenever(repository).stringSet(R.string.filter_ssid_key, setOf())
        // execute
        val actual = fixture.findSSIDs()
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(repository).stringSet(R.string.filter_ssid_key, setOf())
    }

    @Test
    fun saveSSIDs() {
        // setup
        val values: Set<String> = setOf("value1", "value2", "value3")
        doNothing().whenever(repository).saveStringSet(R.string.filter_ssid_key, values)
        // execute
        fixture.saveSSIDs(values)
        // validate
        verify(repository).saveStringSet(R.string.filter_ssid_key, values)
    }

    @Test
    fun findWiFiBands() {
        // setup
        val expected = WiFiBand.GHZ5
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(WiFiBand.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_wifi_band_key, defaultValues)
        // execute
        val actual = fixture.findWiFiBands()
        // validate
        assertThat(actual).hasSize(1)
        assertThat(actual).contains(expected)
        verify(repository).stringSet(R.string.filter_wifi_band_key, defaultValues)
    }

    @Test
    fun saveWiFiBands() {
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
    fun findStrengths() {
        // setup
        val expected = Strength.THREE
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(Strength.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_strength_key, defaultValues)
        // execute
        val actual = fixture.findStrengths()
        // validate
        assertThat(actual).hasSize(1)
        assertThat(actual).contains(expected)
        verify(repository).stringSet(R.string.filter_strength_key, defaultValues)
    }

    @Test
    fun saveStrengths() {
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
    fun findSecurities() {
        // setup
        val expected = Security.WPA
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(Security.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_security_key, defaultValues)
        // execute
        val actual = fixture.findSecurities()
        // validate
        assertThat(actual).hasSize(1)
        assertThat(actual).contains(expected)
        verify(repository).stringSet(R.string.filter_security_key, defaultValues)
    }

    @Test
    fun saveSecurities() {
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
    fun countryCode() {
        // setup
        val defaultValue = defaultCountryCode()
        val expected = "WW"
        doReturn(expected).whenever(repository).string(R.string.country_code_key, defaultValue)
        // execute
        val actual = fixture.countryCode()
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(repository).string(R.string.country_code_key, defaultValue)
    }

    @Test
    fun languageLocale() {
        // setup
        val defaultValue = defaultLanguageTag()
        val expected = Locale.FRENCH
        doReturn(toLanguageTag(expected)).whenever(repository).string(R.string.language_key, defaultValue)
        // execute
        val actual = fixture.languageLocale()
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(repository).string(R.string.language_key, defaultValue)
    }

    @Test
    fun selectedMenu() {
        // setup
        doReturn(NavigationMenu.CHANNEL_GRAPH.ordinal)
            .whenever(repository).stringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)
        // execute
        val actual = fixture.selectedMenu()
        // validate
        assertThat(actual).isEqualTo(NavigationMenu.CHANNEL_GRAPH)
        verify(repository).stringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)
    }

    @Test
    fun saveSelectedMenu() {
        // setup
        doNothing().whenever(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
        // execute
        fixture.saveSelectedMenu(NavigationMenu.CHANNEL_GRAPH)
        // validate
        verify(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
    }

    @Test
    fun saveSelectedMenuWithNotAllowedMenu() {
        // execute
        fixture.saveSelectedMenu(NavigationMenu.ABOUT)
    }

    @Test
    fun wiFiOffOnExit() {
        // execute
        val actual = fixture.wiFiOffOnExit()
        // validate
        assertThat(actual).isFalse()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun wiFiOffOnExitLegacy() {
        // setup
        doReturn(true).whenever(repository).resourceBoolean(R.bool.wifi_off_on_exit_default)
        doReturn(true).whenever(repository).boolean(R.string.wifi_off_on_exit_key, true)
        // execute
        val actual = fixture.wiFiOffOnExit()
        // validate
        assertThat(actual).isTrue()
        verify(repository).boolean(R.string.wifi_off_on_exit_key, true)
        verify(repository).resourceBoolean(R.bool.wifi_off_on_exit_default)
    }

    @Test
    fun keepScreenOn() {
        // setup
        doReturn(true).whenever(repository).resourceBoolean(R.bool.keep_screen_on_default)
        doReturn(true).whenever(repository).boolean(R.string.keep_screen_on_key, true)
        // execute
        val actual = fixture.keepScreenOn()
        // validate
        assertThat(actual).isTrue()
        verify(repository).boolean(R.string.keep_screen_on_key, true)
        verify(repository).resourceBoolean(R.bool.keep_screen_on_default)
    }

    @Test
    fun cacheOff() {
        // setup
        doReturn(true).whenever(repository).resourceBoolean(R.bool.cache_off_default)
        doReturn(true).whenever(repository).boolean(R.string.cache_off_key, true)
        // execute
        val actual = fixture.cacheOff()
        // validate
        assertThat(actual).isTrue()
        verify(repository).boolean(R.string.cache_off_key, true)
        verify(repository).resourceBoolean(R.bool.cache_off_default)
    }

}