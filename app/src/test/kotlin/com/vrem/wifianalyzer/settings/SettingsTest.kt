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
import com.vrem.wifianalyzer.wifi.model.GroupBy
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.SortBy
import com.vrem.wifianalyzer.wifi.model.Strength
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class SettingsTest {
    private val scanSpeedDefault = 5
    private val graphYMultiplier = -10
    private val graphYDefault = 2

    private val repository: Repository = mock()
    private val onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener = mock()
    private val fixture = Settings(repository)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
        verifyNoMoreInteractions(onSharedPreferenceChangeListener)
    }

    @Test
    fun initializeDefaultValues() {
        // Arrange
        doNothing().whenever(repository).initializeDefaultValues()
        // Act
        fixture.initializeDefaultValues()
        // Assert
        verify(repository).initializeDefaultValues()
    }

    @Test
    fun registerOnSharedPreferenceChangeListener() {
        // Arrange
        doNothing().whenever(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // Act
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // Assert
        verify(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    @Test
    fun scanSpeed() {
        // Arrange
        val defaultValue = scanSpeedDefault - 2
        val speedValue = scanSpeedDefault - 1
        doReturn(defaultValue).whenever(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        doReturn(speedValue).whenever(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
        // Act
        val actual = fixture.scanSpeed()
        // Assert
        assertThat(actual).isEqualTo(speedValue)
        verify(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        verify(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
    }

    @Test
    fun graphMaximumY() {
        // Arrange
        val defaultValue = 1
        val value = 2
        val expected = value * graphYMultiplier
        doReturn(defaultValue).whenever(repository).stringAsInteger(R.string.graph_maximum_y_default, graphYDefault)
        doReturn(value).whenever(repository).stringAsInteger(R.string.graph_maximum_y_key, defaultValue)
        // Act
        val actual = fixture.graphMaximumY()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(repository).stringAsInteger(R.string.graph_maximum_y_default, graphYDefault)
        verify(repository).stringAsInteger(R.string.graph_maximum_y_key, defaultValue)
    }

    @Test
    fun groupBy() {
        // Arrange
        doReturn(GroupBy.CHANNEL.ordinal)
            .whenever(repository)
            .stringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)
        // Act
        val actual = fixture.groupBy()
        // Assert
        assertThat(actual).isEqualTo(GroupBy.CHANNEL)
        verify(repository).stringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal)
    }

    @Test
    fun sortBy() {
        // Arrange
        doReturn(SortBy.SSID.ordinal)
            .whenever(repository)
            .stringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)
        // Act
        val actual = fixture.sortBy()
        // Assert
        assertThat(actual).isEqualTo(SortBy.SSID)
        verify(repository).stringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal)
    }

    @Test
    fun accessPointView() {
        // Arrange
        doReturn(AccessPointViewType.COMPACT.ordinal)
            .whenever(repository)
            .stringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)
        // Act
        val actual = fixture.accessPointView()
        // Assert
        assertThat(actual).isEqualTo(AccessPointViewType.COMPACT)
        verify(repository).stringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal)
    }

    @Test
    fun connectionViewType() {
        // Arrange
        doReturn(ConnectionViewType.COMPLETE.ordinal)
            .whenever(repository)
            .stringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPACT.ordinal)
        // Act
        val actual = fixture.connectionViewType()
        // Assert
        assertThat(actual).isEqualTo(ConnectionViewType.COMPLETE)
        verify(repository).stringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPACT.ordinal)
    }

    @Test
    fun themeStyle() {
        ThemeStyle.entries.forEach {
            // Arrange
            doReturn(it.ordinal)
                .whenever(repository)
                .stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
            // Act
            val actual = fixture.themeStyle()
            // Assert
            assertThat(actual).describedAs("Theme: $it").isEqualTo(it)
        }
        verify(repository, times(ThemeStyle.entries.size))
            .stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
    }

    @Test
    fun themeStyleInvalid() {
        // Arrange
        doReturn(ThemeStyle.entries.size)
            .whenever(repository)
            .stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
        // Act
        val actual = fixture.themeStyle()
        // Assert
        assertThat(actual).isEqualTo(ThemeStyle.DARK)
        verify(repository).stringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal)
    }

    @Test
    fun getWiFiBand() {
        // Arrange
        doReturn(WiFiBand.GHZ5.ordinal)
            .whenever(repository)
            .stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
        // Act
        val actual = fixture.wiFiBand()
        // Assert
        assertThat(actual).isEqualTo(WiFiBand.GHZ5)
        verify(repository).stringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal)
    }

    @Test
    fun setWiFiBand() {
        // Arrange
        doNothing().whenever(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.ordinal)
        // Act
        fixture.wiFiBand(WiFiBand.GHZ5)
        // Assert
        verify(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.ordinal)
    }

    @Test
    fun settingsFindSSIDs() {
        // Arrange
        val expected: Set<String> = setOf("value1", "value2", "value3")
        doReturn(expected).whenever(repository).stringSet(R.string.filter_ssid_key, setOf())
        // Act
        val actual = fixture.findSSIDs()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(repository).stringSet(R.string.filter_ssid_key, setOf())
    }

    @Test
    fun saveSSIDs() {
        // Arrange
        val values: Set<String> = setOf("value1", "value2", "value3")
        doNothing().whenever(repository).saveStringSet(R.string.filter_ssid_key, values)
        // Act
        fixture.saveSSIDs(values)
        // Assert
        verify(repository).saveStringSet(R.string.filter_ssid_key, values)
    }

    @Test
    fun settingsFindWiFiBands() {
        // Arrange
        val expected = WiFiBand.GHZ5
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(WiFiBand.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_wifi_band_key, defaultValues)
        // Act
        val actual = fixture.findWiFiBands()
        // Assert
        assertThat(actual).hasSize(1)
        assertThat(actual).contains(expected)
        verify(repository).stringSet(R.string.filter_wifi_band_key, defaultValues)
    }

    @Test
    fun saveWiFiBands() {
        // Arrange
        val values = setOf(WiFiBand.GHZ5)
        val expected = setOf("" + WiFiBand.GHZ5.ordinal)
        doNothing().whenever(repository).saveStringSet(R.string.filter_wifi_band_key, expected)
        // Act
        fixture.saveWiFiBands(values)
        // Assert
        verify(repository).saveStringSet(R.string.filter_wifi_band_key, expected)
    }

    @Test
    fun settingsFindStrengths() {
        // Arrange
        val expected = Strength.THREE
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(Strength.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_strength_key, defaultValues)
        // Act
        val actual = fixture.findStrengths()
        // Assert
        assertThat(actual).hasSize(1)
        assertThat(actual).contains(expected)
        verify(repository).stringSet(R.string.filter_strength_key, defaultValues)
    }

    @Test
    fun saveStrengths() {
        // Arrange
        val values = setOf(Strength.TWO)
        val expected = setOf("" + Strength.TWO.ordinal)
        doNothing().whenever(repository).saveStringSet(R.string.filter_strength_key, expected)
        // Act
        fixture.saveStrengths(values)
        // Assert
        verify(repository).saveStringSet(R.string.filter_strength_key, expected)
    }

    @Test
    fun settingsFindSecurities() {
        // Arrange
        val expected = Security.WPA
        val values = setOf("" + expected.ordinal)
        val defaultValues = ordinals(Security.entries)
        doReturn(values).whenever(repository).stringSet(R.string.filter_security_key, defaultValues)
        // Act
        val actual = fixture.findSecurities()
        // Assert
        assertThat(actual).hasSize(1)
        assertThat(actual).contains(expected)
        verify(repository).stringSet(R.string.filter_security_key, defaultValues)
    }

    @Test
    fun saveSecurities() {
        // Arrange
        val values = setOf(Security.WEP)
        val expected = setOf("" + Security.WEP.ordinal)
        doNothing().whenever(repository).saveStringSet(R.string.filter_security_key, expected)
        // Act
        fixture.saveSecurities(values)
        // Assert
        verify(repository).saveStringSet(R.string.filter_security_key, expected)
    }

    @Test
    fun countryCode() {
        // Arrange
        val defaultValue = defaultCountryCode()
        val expected = "WW"
        doReturn(expected).whenever(repository).string(R.string.country_code_key, defaultValue)
        // Act
        val actual = fixture.countryCode()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(repository).string(R.string.country_code_key, defaultValue)
    }

    @Test
    fun languageLocale() {
        // Arrange
        val defaultValue = defaultLanguageTag()
        val expected = Locale.FRENCH
        doReturn(toLanguageTag(expected)).whenever(repository).string(R.string.language_key, defaultValue)
        // Act
        val actual = fixture.languageLocale()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(repository).string(R.string.language_key, defaultValue)
    }

    @Test
    fun selectedMenu() {
        // Arrange
        doReturn(NavigationMenu.CHANNEL_GRAPH.ordinal)
            .whenever(repository)
            .stringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)
        // Act
        val actual = fixture.selectedMenu()
        // Assert
        assertThat(actual).isEqualTo(NavigationMenu.CHANNEL_GRAPH)
        verify(repository).stringAsInteger(R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS.ordinal)
    }

    @Test
    fun saveSelectedMenu() {
        // Arrange
        doNothing().whenever(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
        // Act
        fixture.saveSelectedMenu(NavigationMenu.CHANNEL_GRAPH)
        // Assert
        verify(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
    }

    @Test
    fun saveSelectedMenuWithNotAllowedMenu() {
        // Act
        fixture.saveSelectedMenu(NavigationMenu.ABOUT)
    }

    @Test
    fun wiFiOffOnExit() {
        // Act
        val actual = fixture.wiFiOffOnExit()
        // Assert
        assertThat(actual).isFalse
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun wiFiOffOnExitLegacy() {
        // Arrange
        doReturn(true).whenever(repository).resourceBoolean(R.bool.wifi_off_on_exit_default)
        doReturn(true).whenever(repository).boolean(R.string.wifi_off_on_exit_key, true)
        // Act
        val actual = fixture.wiFiOffOnExit()
        // Assert
        assertThat(actual).isTrue
        verify(repository).boolean(R.string.wifi_off_on_exit_key, true)
        verify(repository).resourceBoolean(R.bool.wifi_off_on_exit_default)
    }

    @Test
    fun keepScreenOn() {
        // Arrange
        doReturn(true).whenever(repository).resourceBoolean(R.bool.keep_screen_on_default)
        doReturn(true).whenever(repository).boolean(R.string.keep_screen_on_key, true)
        // Act
        val actual = fixture.keepScreenOn()
        // Assert
        assertThat(actual).isTrue
        verify(repository).boolean(R.string.keep_screen_on_key, true)
        verify(repository).resourceBoolean(R.bool.keep_screen_on_default)
    }

    @Test
    fun cacheOff() {
        // Arrange
        doReturn(true).whenever(repository).resourceBoolean(R.bool.cache_off_default)
        doReturn(true).whenever(repository).boolean(R.string.cache_off_key, true)
        // Act
        val actual = fixture.cacheOff()
        // Assert
        assertThat(actual).isTrue
        verify(repository).boolean(R.string.cache_off_key, true)
        verify(repository).resourceBoolean(R.bool.cache_off_default)
    }
}
