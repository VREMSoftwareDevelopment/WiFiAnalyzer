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
package com.vrem.wifianalyzer.wifi.graphutils

import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R

private fun String.toColor(): Int = this.substring(1).toLong(16).toInt()

data class GraphColor(
    @param:ColorInt val primary: Int,
    @param:ColorInt val background: Int,
)

@OpenClass
class GraphColors {
    private val availableGraphColors: List<GraphColor> by lazy {
        MainContext.INSTANCE.resources
            .getStringArray(R.array.graph_colors)
            .filterNotNull()
            .chunked(2) { GraphColor(it[0].toColor(), it[1].toColor()) }
            .reversed()
    }
    private val currentGraphColors: ArrayDeque<GraphColor> = ArrayDeque()
    val connectedColor: GraphColor by lazy {
        val context = MainContext.INSTANCE.context
        val primary = ContextCompat.getColor(context, R.color.selected)
        val background = ContextCompat.getColor(context, R.color.selected_background)
        GraphColor(primary, background)
    }

    fun graphColor(): GraphColor {
        if (currentGraphColors.isEmpty()) {
            currentGraphColors.addAll(availableGraphColors)
        }
        return currentGraphColors.removeFirst()
    }

    fun addColor(
        @ColorInt primaryColor: Int,
    ) {
        availableGraphColors.firstOrNull { primaryColor == it.primary }?.let {
            if (!currentGraphColors.contains(it)) {
                currentGraphColors.addFirst(it)
            }
        }
    }
}
