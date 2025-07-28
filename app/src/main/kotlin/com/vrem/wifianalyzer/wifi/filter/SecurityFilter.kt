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
import com.vrem.wifianalyzer.wifi.filter.adapter.SecurityAdapter
import com.vrem.wifianalyzer.wifi.model.Security

internal class SecurityFilter(
    securityAdapter: SecurityAdapter,
    alertDialog: AlertDialog,
) : EnumFilter<Security, SecurityAdapter>(
        mapOf(
            Security.NONE to R.id.filterSecurityNone,
            Security.WPS to R.id.filterSecurityWPS,
            Security.WEP to R.id.filterSecurityWEP,
            Security.WPA to R.id.filterSecurityWPA,
            Security.WPA2 to R.id.filterSecurityWPA2,
            Security.WPA3 to R.id.filterSecurityWPA3,
        ),
        securityAdapter,
        alertDialog,
        R.id.filterSecurity,
    )
