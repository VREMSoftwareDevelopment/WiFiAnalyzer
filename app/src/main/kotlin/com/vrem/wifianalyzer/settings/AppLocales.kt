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

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import java.util.Locale

class AppLocales(
    private val context: Context,
) {
    private val systemLocale: Locale? by lazy { LocaleManagerCompat.getSystemLocales(context)[0] }

    fun systemLocale(): Locale? = systemLocale

    fun applicationLocale(): Locale? = AppCompatDelegate.getApplicationLocales()[0]

    fun save(languageTags: String): Unit =
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTags))
}
