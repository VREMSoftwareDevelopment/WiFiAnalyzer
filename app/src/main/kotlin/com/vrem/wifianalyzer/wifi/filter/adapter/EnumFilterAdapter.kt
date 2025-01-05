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
package com.vrem.wifianalyzer.wifi.filter.adapter

import com.vrem.wifianalyzer.R
import kotlin.enums.EnumEntries

abstract class EnumFilterAdapter<T : Enum<T>>(selections: Set<T>, val defaults: EnumEntries<T>) : BasicFilterAdapter<T>(selections) {
    override fun isActive(): Boolean = selections.size != defaults.size

    fun toggle(selection: T): Boolean {
        val size = selections.size
        if (selections.contains(selection)) {
            if (size > 1) {
                selections = selections.minus(selection)
            }
        } else {
            selections = selections.plus(selection)
        }
        return size != selections.size
    }

    override fun reset() {
        selections(defaults.toSet())
    }

    fun color(selection: T): Int = if (selections.contains(selection)) R.color.selected else R.color.regular

    fun contains(selection: T): Boolean = selections.contains(selection)

}