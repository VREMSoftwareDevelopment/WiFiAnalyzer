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
import android.util.AttributeSet
import com.vrem.util.EMPTY
import com.vrem.util.supportedLanguages
import com.vrem.util.toCapitalize
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import java.util.Locale

private fun data(context: Context): List<Data> =
    listOf(Data(String.EMPTY, context.getString(R.string.language_system_default))) +
        supportedLanguages()
            .map { map(it) }
            .sorted()

private fun map(it: Locale): Data = Data(it.toLanguageTag(), it.getDisplayName(it).toCapitalize(it))

class LanguagePreference
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet,
        settings: Settings = MainContext.INSTANCE.settings,
    ) : CustomPreference(context, attrs, data(context), settings.languageTag()) {
        init {
            isPersistent = false
            setOnPreferenceChangeListener { _, newValue ->
                settings.saveLanguageTag(newValue as String)
                true
            }
        }
    }
