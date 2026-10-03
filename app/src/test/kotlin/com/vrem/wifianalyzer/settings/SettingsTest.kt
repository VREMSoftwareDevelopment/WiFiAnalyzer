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
import com.vrem.util.EMPTY
import com.vrem.util.ordinals
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
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.util.Locale

private const val SCAN_SPEED_DEFAULT = 5
private const val GRAPH_Y_MULTIPLIER = -10
private const val GRAPH_Y_DEFAULT = 2

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class SettingsTest {
    private val repository: Repository = mock()
    private val appLocales: AppLocales = mock()
    private val onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener = mock()
    private val fixture = Settings(repository, appLocales)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
        verifyNoMoreInteractions(appLocales)
        verifyNoMoreInteractions(onSharedPreferenceChangeListener)
    }

    @Test
    fun initializeDefaultValuesDelegatesToRepository() {
        // Arrange
        doNothing().whenever(repository).initializeDefaultValues()
        // Act
        fixture.initializeDefaultValues()
        // Assert
        verify(repository).initializeDefaultValues()
    }

    @Test
    fun registerOnSharedPreferenceChangeListenerDelegatesToRepository() {
        // Arrange
        doNothing().whenever(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // Act
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // Assert
        verify(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    @Test
    fun scanSpeedUsesSavedValueAndConfiguredDefault() {
        // Arrange
        val defaultValue = SCAN_SPEED_DEFAULT - 2
        val speedValue = SCAN_SPEED_DEFAULT - 1
        doReturn(defaultValue).whenever(repository).stringAsInteger(R.string.scan_speed_default, SCAN_SPEED_DEFAULT)
        doReturn(speedValue).whenever(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
        // Act
        val actual = fixture.scanSpeed()
        // Assert
        assertThat(actual).isEqualTo(speedValue)
        verify(repository).stringAsInteger(R.string.scan_speed_default, SCAN_SPEED_DEFAULT)
        verify(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
    }

    @Test
    fun graphMaximumYScalesSavedValueUsingConfiguredDefault() {
        // Arrange
        val defaultValue = 1
        val value = 2
        val expected = value * GRAPH_Y_MULTIPLIER
        doReturn(defaultValue).whenever(repository).stringAsInteger(R.string.graph_maximum_y_default, GRAPH_Y_DEFAULT)
        doReturn(value).whenever(repository).stringAsInteger(R.string.graph_maximum_y_key, defaultValue)
        // Act
        val actual = fixture.graphMaximumY()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(repository).stringAsInteger(R.string.graph_maximum_y_default, GRAPH_Y_DEFAULT)
        verify(repository).stringAsInteger(R.string.graph_maximum_y_key, defaultValue)
    }

    @Test
    fun groupByUsesSavedOrdinal() {
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
    fun sortByUsesSavedOrdinal() {
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
    fun accessPointViewUsesSavedOrdinal() {
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
    fun connectionViewTypeUsesSavedOrdinal() {
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
    fun themeStyleReturnsEachSavedValue() {
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
    fun themeStyleFallsBackToDefaultForUnknownOrdinal() {
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
    fun wiFiBandReturnsSavedValue() {
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
    fun wiFiBandSetterSavesSelectedValue() {
        // Arrange
        doNothing().whenever(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.ordinal)
        // Act
        fixture.wiFiBand(WiFiBand.GHZ5)
        // Assert
        verify(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.ordinal)
    }

    @Test
    fun findSSIDsReturnsSavedValues() {
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
    fun saveSSIDsPersistsValues() {
        // Arrange
        val values: Set<String> = setOf("value1", "value2", "value3")
        doNothing().whenever(repository).saveStringSet(R.string.filter_ssid_key, values)
        // Act
        fixture.saveSSIDs(values)
        // Assert
        verify(repository).saveStringSet(R.string.filter_ssid_key, values)
    }

    @Test
    fun findWiFiBandsReturnsSavedValues() {
        // Arrange
        val expected = WiFiBand.GHZ5
        val values = setOf(expected.ordinal.toString())
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
    fun saveWiFiBandsPersistsValues() {
        // Arrange
        val values = setOf(WiFiBand.GHZ5)
        val expected = setOf(WiFiBand.GHZ5.ordinal.toString())
        doNothing().whenever(repository).saveStringSet(R.string.filter_wifi_band_key, expected)
        // Act
        fixture.saveWiFiBands(values)
        // Assert
        verify(repository).saveStringSet(R.string.filter_wifi_band_key, expected)
    }

    @Test
    fun findStrengthsReturnsSavedValues() {
        // Arrange
        val expected = Strength.THREE
        val values = setOf(expected.ordinal.toString())
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
    fun saveStrengthsPersistsValues() {
        // Arrange
        val values = setOf(Strength.TWO)
        val expected = setOf(Strength.TWO.ordinal.toString())
        doNothing().whenever(repository).saveStringSet(R.string.filter_strength_key, expected)
        // Act
        fixture.saveStrengths(values)
        // Assert
        verify(repository).saveStringSet(R.string.filter_strength_key, expected)
    }

    @Test
    fun findSecuritiesReturnsSavedValues() {
        // Arrange
        val expected = Security.WPA
        val values = setOf(expected.ordinal.toString())
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
    fun saveSecuritiesPersistsValues() {
        // Arrange
        val values = setOf(Security.WEP)
        val expected = setOf(Security.WEP.ordinal.toString())
        doNothing().whenever(repository).saveStringSet(R.string.filter_security_key, expected)
        // Act
        fixture.saveSecurities(values)
        // Assert
        verify(repository).saveStringSet(R.string.filter_security_key, expected)
    }

    @Test
    fun countryCodeUsesSavedValueAndSystemLocaleDefault() {
        // Arrange
        val defaultValue = "CA"
        val expected = "WW"
        doReturn(Locale.CANADA).whenever(appLocales).systemLocale()
        doReturn(expected).whenever(repository).string(R.string.country_code_key, defaultValue)
        // Act
        val actual = fixture.countryCode()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(appLocales).systemLocale()
        verify(repository).string(R.string.country_code_key, defaultValue)
    }

    @Test
    fun defaultCountryCodeUsesSystemLocale() {
        // Arrange
        val expected = "CA"
        doReturn(Locale.CANADA).whenever(appLocales).systemLocale()
        // Act
        val actual = fixture.defaultCountryCode()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(appLocales).systemLocale()
    }

    @Test
    fun defaultCountryCodeIsEmptyWithoutSystemLocale() {
        // Arrange
        doReturn(null).whenever(appLocales).systemLocale()
        // Act
        val actual = fixture.defaultCountryCode()
        // Assert
        assertThat(actual).isEmpty()
        verify(appLocales).systemLocale()
    }

    @Test
    fun languageLocaleUsesApplicationLocale() {
        // Arrange
        doReturn(Locale.FRENCH).whenever(appLocales).applicationLocale()
        // Act
        val actual = fixture.languageLocale()
        // Assert
        assertThat(actual).isEqualTo(Locale.FRENCH)
        verify(appLocales).applicationLocale()
    }

    @Test
    fun languageLocaleFallsBackToSystemDefault() {
        // Arrange
        doReturn(null).whenever(appLocales).applicationLocale()
        // Act
        val actual = fixture.languageLocale()
        // Assert
        assertThat(actual).isEqualTo(Locale.getDefault())
        verify(appLocales).applicationLocale()
    }

    @Test
    fun languageTagUsesSupportedApplicationLocale() {
        // Arrange
        doReturn(Locale.forLanguageTag("pt-BR")).whenever(appLocales).applicationLocale()
        // Act
        val actual = fixture.languageTag()
        // Assert
        assertThat(actual).isEqualTo("pt-BR")
        verify(appLocales).applicationLocale()
    }

    @Test
    fun languageTagUsesSupportedRegionalLanguage() {
        // Arrange
        doReturn(Locale.forLanguageTag("zh-Hant-HK")).whenever(appLocales).applicationLocale()
        // Act
        val actual = fixture.languageTag()
        // Assert
        assertThat(actual).isEqualTo("zh-TW")
        verify(appLocales).applicationLocale()
    }

    @Test
    fun languageTagIsEmptyForUnsupportedLanguage() {
        // Arrange
        doReturn(Locale.forLanguageTag("ko-KR")).whenever(appLocales).applicationLocale()
        // Act
        val actual = fixture.languageTag()
        // Assert
        assertThat(actual).isEmpty()
        verify(appLocales).applicationLocale()
    }

    @Test
    fun languageTagIsEmptyWithoutApplicationLocale() {
        // Arrange
        doReturn(null).whenever(appLocales).applicationLocale()
        // Act
        val actual = fixture.languageTag()
        // Assert
        assertThat(actual).isEmpty()
        verify(appLocales).applicationLocale()
    }

    @Test
    fun saveLanguageTagDelegatesToAppLocales() {
        // Arrange
        doNothing().whenever(appLocales).save("de")
        // Act
        fixture.saveLanguageTag("de")
        // Assert
        verify(appLocales).save("de")
    }

    @Test
    fun migrateLanguageDoesNothingWhenLegacyTagIsEmpty() {
        // Arrange
        doReturn(String.EMPTY).whenever(repository).string(R.string.language_key, String.EMPTY)
        // Act
        fixture.migrateLanguage()
        // Assert
        verify(repository).string(R.string.language_key, String.EMPTY)
    }

    @Test
    fun migrateLanguageConvertsSupportedLegacyTag() {
        // Arrange
        doReturn("pt_BR").whenever(repository).string(R.string.language_key, String.EMPTY)
        doReturn(null).whenever(appLocales).applicationLocale()
        doReturn(Locale.US).whenever(appLocales).systemLocale()
        doNothing().whenever(appLocales).save("pt-BR")
        doNothing().whenever(repository).remove(R.string.language_key)
        // Act
        fixture.migrateLanguage()
        // Assert
        verify(repository).string(R.string.language_key, String.EMPTY)
        verify(appLocales).applicationLocale()
        verify(appLocales).systemLocale()
        verify(appLocales).save("pt-BR")
        verify(repository).remove(R.string.language_key)
    }

    @Test
    fun migrateLanguageRemovesUnsupportedLegacyTagWithoutSavingLocale() {
        // Arrange
        doReturn("en_US").whenever(repository).string(R.string.language_key, String.EMPTY)
        doReturn(null).whenever(appLocales).applicationLocale()
        doReturn(Locale.CANADA_FRENCH).whenever(appLocales).systemLocale()
        doNothing().whenever(repository).remove(R.string.language_key)
        // Act
        fixture.migrateLanguage()
        // Assert
        verify(repository).string(R.string.language_key, String.EMPTY)
        verify(appLocales).applicationLocale()
        verify(appLocales).systemLocale()
        verify(repository).remove(R.string.language_key)
    }

    @Test
    fun migrateLanguagePreservesExistingApplicationLocale() {
        // Arrange
        doReturn("de_").whenever(repository).string(R.string.language_key, String.EMPTY)
        doReturn(Locale.FRENCH).whenever(appLocales).applicationLocale()
        doNothing().whenever(repository).remove(R.string.language_key)
        // Act
        fixture.migrateLanguage()
        // Assert
        verify(repository).string(R.string.language_key, String.EMPTY)
        verify(appLocales).applicationLocale()
        verify(repository).remove(R.string.language_key)
    }

    @Test
    fun migrateLanguageKeepsSystemDefaultWhenLegacyTagMatches() {
        // Arrange
        doReturn("pt_BR").whenever(repository).string(R.string.language_key, String.EMPTY)
        doReturn(null).whenever(appLocales).applicationLocale()
        doReturn(Locale.forLanguageTag("pt-BR")).whenever(appLocales).systemLocale()
        doNothing().whenever(repository).remove(R.string.language_key)
        // Act
        fixture.migrateLanguage()
        // Assert
        verify(repository).string(R.string.language_key, String.EMPTY)
        verify(appLocales).applicationLocale()
        verify(appLocales).systemLocale()
        verify(appLocales, never()).save("pt-BR")
        verify(repository).remove(R.string.language_key)
    }

    @Test
    fun migrateLanguageConvertsSupportedLegacyTagWithoutSystemLocale() {
        // Arrange
        doReturn("pt_BR").whenever(repository).string(R.string.language_key, String.EMPTY)
        doReturn(null).whenever(appLocales).applicationLocale()
        doReturn(null).whenever(appLocales).systemLocale()
        doNothing().whenever(appLocales).save("pt-BR")
        doNothing().whenever(repository).remove(R.string.language_key)
        // Act
        fixture.migrateLanguage()
        // Assert
        verify(repository).string(R.string.language_key, String.EMPTY)
        verify(appLocales).applicationLocale()
        verify(appLocales).systemLocale()
        verify(appLocales).save("pt-BR")
        verify(repository).remove(R.string.language_key)
    }

    @Test
    fun selectedMenuUsesSavedOrdinal() {
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
    fun saveSelectedMenuPersistsAllowedMenu() {
        // Arrange
        doNothing().whenever(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
        // Act
        fixture.saveSelectedMenu(NavigationMenu.CHANNEL_GRAPH)
        // Assert
        verify(repository).save(R.string.selected_menu_key, NavigationMenu.CHANNEL_GRAPH.ordinal)
    }

    @Test
    fun saveSelectedMenuDoesNotPersistMenuOutsideMainNavigation() {
        // Act
        fixture.saveSelectedMenu(NavigationMenu.ABOUT)
    }

    @Test
    fun wiFiOffOnExitIsFalseOnModernAndroid() {
        // Act
        val actual = fixture.wiFiOffOnExit()
        // Assert
        assertThat(actual).isFalse
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun wiFiOffOnExitUsesSavedValueOnLegacyAndroid() {
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
    fun keepScreenOnUsesSavedValueAndConfiguredDefault() {
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
    fun cacheOffUsesSavedValueAndConfiguredDefault() {
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
