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
package com.vrem.wifianalyzer.wifi.graphutils

import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R

private fun String.toColor(): Long = this.substring(1).toLong(16)

data class GraphColor(val primary: Long, val background: Long)

internal val transparent = GraphColor(0x009E9E9E, 0x009E9E9E)

@OpenClass
class GraphColors {
    private val availableGraphColors: MutableList<GraphColor> = mutableListOf()
    private val currentGraphColors: ArrayDeque<GraphColor> = ArrayDeque()

    private fun availableGraphColors(): List<GraphColor> {
        if (availableGraphColors.isEmpty()) {
            val colors = MainContext.INSTANCE.resources.getStringArray(R.array.graph_colors)
                .filterNotNull()
                .withIndex()
                .groupBy { it.index / 2 }
                .map { GraphColor(it.value[0].value.toColor(), it.value[1].value.toColor()) }
                .reversed()
            availableGraphColors.addAll(colors)
        }
        return availableGraphColors
    }

    fun graphColor(): GraphColor {
        if (currentGraphColors.isEmpty()) {
            currentGraphColors.addAll(availableGraphColors())
        }
        return currentGraphColors.removeFirst()
    }

    fun addColor(primaryColor: Long) {
        availableGraphColors().firstOrNull { primaryColor == it.primary }?.let {
            if (!currentGraphColors.contains(it)) {
                currentGraphColors.addFirst(it)
            }
        }
    }

}


