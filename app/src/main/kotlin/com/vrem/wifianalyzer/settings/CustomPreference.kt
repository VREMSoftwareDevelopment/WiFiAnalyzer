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
import androidx.preference.ListPreference

abstract class CustomPreference(context: Context, attrs: AttributeSet, values: List<Data>, defaultValue: String) :
    ListPreference(context, attrs) {
    init {
        this.entries = names(values)
        this.entryValues = codes(values)
        this.setDefaultValue(defaultValue)
    }

    private fun codes(data: List<Data>): Array<CharSequence> {
        return data.map { it.code }.toTypedArray()
    }

    private fun names(data: List<Data>): Array<CharSequence> {
        return data.map { it.name }.toTypedArray()
    }

}