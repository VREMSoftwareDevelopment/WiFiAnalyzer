/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.vrem.util.EMPTY
import com.vrem.util.buildMinVersionT
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

typealias SecurityTypeId = Int

enum class WiFiSecurityType(
    val securityTypeId: SecurityTypeId,
    @StringRes val textResource: Int,
    val security: Security = Security.NONE
) {
    UNKNOWN(-1, R.string.security_type_unknown),
    OPEN(0, R.string.security_type_open),
    WEP(1, R.string.security_type_wep, Security.WEP),
    PSK(2, R.string.security_type_psk),
    EAP(3, R.string.security_type_eap),
    SAE(4, R.string.security_type_sae, Security.WPA3),
    EAP_WPA3_ENTERPRISE_192_BIT(5, R.string.security_type_eap_wpa3_enterprise_192_bit, Security.WPA3),
    OWE(6, R.string.security_type_owe, Security.WPA3),
    WAPI_PSK(7, R.string.security_type_wapi_psk),
    WAPI_CERT(8, R.string.security_type_wapi_cert),
    EAP_WPA3_ENTERPRISE(9, R.string.security_type_eap_wpa3_enterprise, Security.WPA3),
    OSEN(10, R.string.security_type_osen),
    PASSPOINT_R1_R2(11, R.string.security_type_passpoint_r1_r2),
    PASSPOINT_R3(12, R.string.security_type_passpoint_r3),
    SECURITY_TYPE_DPP(13, R.string.security_type_dpp);

    companion object {
        fun findOne(securityTypeId: SecurityTypeId) =
            entries.firstOrNull { it.securityTypeId == securityTypeId } ?: UNKNOWN

        fun findAll(securityTypes: List<Int>): Set<WiFiSecurityType> = securityTypes.map { findOne(it) }.toSet()

        fun find(scanResult: ScanResult): List<Int> =
            if (buildMinVersionT()) {
                securityTypeValues(scanResult)
            } else {
                listOf()
            }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private fun securityTypeValues(scanResult: ScanResult) = scanResult.securityTypes.asList()
    }
}

data class WiFiSecurity(val capabilities: String = String.EMPTY, val securityTypes: List<Int> = listOf()) {

    val security: Security
        get() = securities.first()

    val securities: Set<Security>
        get() {
            return (transformCapabilities() + transformSecurityTypes()).toSortedSet().ifEmpty { setOf(Security.NONE) }
        }

    val wiFiSecurityTypes: Set<WiFiSecurityType>
        get() {
            return WiFiSecurityType.findAll(securityTypes)
        }

    fun wiFiSecurityTypesDisplay(context: Context): String =
        wiFiSecurityTypes
            .map { securityType -> context.getString(securityType.textResource) }
            .filter { text -> text.isNotBlank() }
            .toList()
            .joinToString(" ", "[", "]")

    private fun transformCapabilities(): Set<Security> =
        regex.replace(capabilities.uppercase(Locale.getDefault()), "-")
            .split("-")
            .filter { it.isNotBlank() && it != Security.NONE.name }
            .mapNotNull { transformCapability(it) }
            .toSet()

    private fun transformCapability(name: String) =
        Security.entries.firstOrNull { security -> security.name == name }
            ?: Security.entries.firstOrNull { security -> security.extras.contains(name) }

    private fun transformSecurityTypes(): Set<Security> =
        wiFiSecurityTypes.filter { it.security != Security.NONE }.map { it.security }.toSet()

    companion object {
        val EMPTY = WiFiSecurity()
    }
}
