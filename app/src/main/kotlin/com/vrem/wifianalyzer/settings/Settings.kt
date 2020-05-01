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
import com.vrem.annotation.OpenClass
import com.vrem.util.*
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.NavigationGroup
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointViewType
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionViewType
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.graphutils.GraphLegend
import com.vrem.wifianalyzer.wifi.model.GroupBy
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.SortBy
import com.vrem.wifianalyzer.wifi.model.Strength
import java.util.*

@OpenClass
class Settings(private val repository: Repository) {
    fun initializeDefaultValues() {
        repository.initializeDefaultValues()
    }

    fun registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener): Unit =
            repository.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)

    fun getScanSpeed(): Int {
        val defaultValue = repository.getStringAsInteger(R.string.scan_speed_default, SCAN_SPEED_DEFAULT)
        val scanSpeed = repository.getStringAsInteger(R.string.scan_speed_key, defaultValue)
        return if (versionP()) {
            if (isWiFiThrottleDisabled()) scanSpeed.coerceAtLeast(SCAN_SPEED_DEFAULT) else scanSpeed
        } else scanSpeed
    }

    fun isWiFiThrottleDisabled(): Boolean {
        if (versionP()) {
            val defaultValue = repository.getResourceBoolean(R.bool.wifi_throttle_disabled_default)
            return repository.getBoolean(R.string.wifi_throttle_disabled_key, defaultValue)
        }
        return false
    }

    fun getGraphMaximumY(): Int {
        val defaultValue = repository.getStringAsInteger(R.string.graph_maximum_y_default, GRAPH_Y_DEFAULT)
        val result = repository.getStringAsInteger(R.string.graph_maximum_y_key, defaultValue)
        return result * GRAPH_Y_MULTIPLIER
    }

    fun toggleWiFiBand() {
        repository.save(R.string.wifi_band_key, getWiFiBand().toggle().ordinal)
    }

    fun getCountryCode(): String {
        val countryCode = defaultCountryCode()
        return repository.getString(R.string.country_code_key, countryCode)
    }

    fun getLanguageLocale(): Locale {
        val defaultLanguageTag = defaultLanguageTag()
        val languageTag = repository.getString(R.string.language_key, defaultLanguageTag)
        return findByLanguageTag(languageTag)
    }

    fun getSortBy(): SortBy =
            find(SortBy::class.java, R.string.sort_by_key, SortBy.STRENGTH)

    fun getGroupBy(): GroupBy =
            find(GroupBy::class.java, R.string.group_by_key, GroupBy.NONE)

    fun getAccessPointView(): AccessPointViewType =
            find(AccessPointViewType::class.java, R.string.ap_view_key, AccessPointViewType.COMPLETE)

    fun getConnectionViewType(): ConnectionViewType =
            find(ConnectionViewType::class.java, R.string.connection_view_key, ConnectionViewType.COMPLETE)

    fun getChannelGraphLegend(): GraphLegend =
            find(GraphLegend::class.java, R.string.channel_graph_legend_key, GraphLegend.HIDE)

    fun getTimeGraphLegend(): GraphLegend =
            find(GraphLegend::class.java, R.string.time_graph_legend_key, GraphLegend.LEFT)

    fun getWiFiBand(): WiFiBand =
            find(WiFiBand::class.java, R.string.wifi_band_key, WiFiBand.GHZ2)

    fun isWiFiOffOnExit(): Boolean =
            if (minVersionQ()) {
                false
            } else {
                repository.getBoolean(R.string.wifi_off_on_exit_key, repository.getResourceBoolean(R.bool.wifi_off_on_exit_default))
            }

    fun isKeepScreenOn(): Boolean =
            repository.getBoolean(R.string.keep_screen_on_key, repository.getResourceBoolean(R.bool.keep_screen_on_default))

    fun getThemeStyle(): ThemeStyle =
            find(ThemeStyle::class.java, R.string.theme_key, ThemeStyle.DARK)

    fun getSelectedMenu(): NavigationMenu =
            find(NavigationMenu::class.java, R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS)

    fun saveSelectedMenu(navigationMenu: NavigationMenu) {
        if (NavigationGroup.GROUP_FEATURE.navigationMenus.contains(navigationMenu)) {
            repository.save(R.string.selected_menu_key, navigationMenu.ordinal)
        }
    }

    fun getSSIDs(): Set<String> =
            repository.getStringSet(R.string.filter_ssid_key, HashSet())

    fun saveSSIDs(values: Set<String>): Unit =
            repository.saveStringSet(R.string.filter_ssid_key, values)

    fun getWiFiBands(): Set<WiFiBand> =
            findSet(WiFiBand::class.java, R.string.filter_wifi_band_key, WiFiBand.GHZ2)

    fun saveWiFiBands(values: Set<WiFiBand>): Unit =
            saveSet(R.string.filter_wifi_band_key, values)

    fun getStrengths(): Set<Strength> =
            findSet(Strength::class.java, R.string.filter_strength_key, Strength.FOUR)

    fun saveStrengths(values: Set<Strength>): Unit =
            saveSet(R.string.filter_strength_key, values)

    fun getSecurities(): Set<Security> =
            findSet(Security::class.java, R.string.filter_security_key, Security.NONE)

    fun saveSecurities(values: Set<Security>): Unit =
            saveSet(R.string.filter_security_key, values)

    private fun <T : Enum<*>> find(enumType: Class<T>, key: Int, defaultValue: T): T {
        val value = repository.getStringAsInteger(key, defaultValue.ordinal)
        return EnumUtils.find(enumType, value, defaultValue)
    }

    private fun <T : Enum<*>> findSet(enumType: Class<T>, key: Int, defaultValue: T): Set<T> {
        val defaultValues = EnumUtils.ordinals(enumType)
        val values = repository.getStringSet(key, defaultValues)
        return EnumUtils.find(enumType, values, defaultValue)
    }

    private fun <T : Enum<*>?> saveSet(key: Int, values: Set<T>): Unit =
            repository.saveStringSet(key, EnumUtils.find(values))

    fun minVersionQ(): Boolean = isMinVersionQ()

    fun versionP(): Boolean = isVersionP()

    companion object {
        const val SCAN_SPEED_DEFAULT = 5
        const val GRAPH_Y_MULTIPLIER = -10
        const val GRAPH_Y_DEFAULT = 2
    }

}