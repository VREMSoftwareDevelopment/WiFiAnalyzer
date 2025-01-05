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
package com.vrem.wifianalyzer

import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionViewType
import java.util.Locale

class MainReload(settings: Settings) {
    var themeStyle: ThemeStyle
        private set
    var connectionViewType: ConnectionViewType
        private set
    var languageLocale: Locale
        private set

    fun shouldReload(settings: Settings): Boolean =
        themeChanged(settings) || connectionViewTypeChanged(settings) || languageChanged(settings)

    private fun connectionViewTypeChanged(settings: Settings): Boolean {
        val currentConnectionViewType = settings.connectionViewType()
        val connectionViewTypeChanged = connectionViewType != currentConnectionViewType
        if (connectionViewTypeChanged) {
            connectionViewType = currentConnectionViewType
        }
        return connectionViewTypeChanged
    }

    private fun themeChanged(settings: Settings): Boolean {
        val settingThemeStyle = settings.themeStyle()
        val themeChanged = themeStyle != settingThemeStyle
        if (themeChanged) {
            themeStyle = settingThemeStyle
        }
        return themeChanged
    }

    private fun languageChanged(settings: Settings): Boolean {
        val settingLanguageLocale = settings.languageLocale()
        val languageLocaleChanged = languageLocale != settingLanguageLocale
        if (languageLocaleChanged) {
            languageLocale = settingLanguageLocale
        }
        return languageLocaleChanged
    }

    init {
        themeStyle = settings.themeStyle()
        connectionViewType = settings.connectionViewType()
        languageLocale = settings.languageLocale()
    }
}