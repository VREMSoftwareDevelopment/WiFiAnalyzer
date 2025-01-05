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
package com.vrem.wifianalyzer.wifi.filter

import android.app.AlertDialog
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.filter.adapter.StrengthAdapter
import com.vrem.wifianalyzer.wifi.model.Strength

internal class StrengthFilter(strengthAdapter: StrengthAdapter, alertDialog: AlertDialog) :
    EnumFilter<Strength, StrengthAdapter>(
        mapOf(
            Strength.ZERO to R.id.filterStrength0,
            Strength.ONE to R.id.filterStrength1,
            Strength.TWO to R.id.filterStrength2,
            Strength.THREE to R.id.filterStrength3,
            Strength.FOUR to R.id.filterStrength4
        ),
        strengthAdapter,
        alertDialog,
        R.id.filterStrength
    )