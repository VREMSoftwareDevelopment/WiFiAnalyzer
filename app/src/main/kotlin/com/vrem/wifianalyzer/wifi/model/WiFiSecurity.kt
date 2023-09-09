/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.model

import androidx.annotation.DrawableRes
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.R
import java.util.Locale

private val extras = listOf("SAE", "EAP_SUITE_B_192", "OWE")
private val regex = Regex("[^A-Z0-9_]")

enum class Security(@DrawableRes val imageResource: Int, val extras: List<String> = listOf()) {
    NONE(R.drawable.ic_lock_open),
    WPS(R.drawable.ic_lock_outline),
    WEP(R.drawable.ic_lock_outline),
    WPA(R.drawable.ic_lock),
    WPA2(R.drawable.ic_lock),
    WPA3(R.drawable.ic_lock, extras);
}

data class WiFiSecurity(val capabilities: String = String.EMPTY, val securityTypes: List<Int> = listOf()) {

    val security: Security
        get() = securities.first()

    val securities: Set<Security>
        get() = parse(capabilities).mapNotNull { transform(it) }.toSortedSet().ifEmpty { setOf(Security.NONE) }

    private fun transform(name: String) =
        Security.entries.find { security -> security.name == name } ?: Security.entries.find { security -> security.extras.contains(name) }

    private fun parse(capabilities: String): List<String> =
        regex.replace(capabilities.uppercase(Locale.getDefault()), "-")
            .split("-")
            .filter { it.isNotBlank() && it != Security.NONE.name }

    companion object {
        val EMPTY = WiFiSecurity()
    }
}
