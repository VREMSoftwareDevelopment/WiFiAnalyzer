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

import android.content.Context
import android.util.AttributeSet
import com.vrem.util.defaultLanguageTag
import com.vrem.util.supportedLanguages
import com.vrem.util.toCapitalize
import com.vrem.util.toLanguageTag
import java.util.Locale

private fun data(): List<Data> = supportedLanguages()
    .map { map(it) }
    .sorted()

private fun map(it: Locale): Data =
    Data(toLanguageTag(it), it.getDisplayName(it).toCapitalize(Locale.getDefault()))

class LanguagePreference(context: Context, attrs: AttributeSet) :
    CustomPreference(context, attrs, data(), defaultLanguageTag())