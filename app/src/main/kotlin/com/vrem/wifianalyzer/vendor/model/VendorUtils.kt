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
package com.vrem.wifianalyzer.vendor.model

import com.vrem.util.EMPTY
import java.util.Locale

internal const val MAX_SIZE = 6
private const val SEPARATOR = ":"

internal fun String.clean(): String =
    orEmpty().replace(SEPARATOR, String.EMPTY).take(MAX_SIZE).uppercase(Locale.getDefault())

internal fun String.toMacAddress(): String =
    when {
        isEmpty() -> String.EMPTY
        length < MAX_SIZE -> "*$this*"
        else -> substring(0, 2) + SEPARATOR + substring(2, 4) + SEPARATOR + substring(4, 6)
    }
