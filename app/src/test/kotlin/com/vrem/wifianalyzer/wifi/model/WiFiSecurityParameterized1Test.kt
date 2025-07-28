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
package com.vrem.wifianalyzer.wifi.model

import android.net.wifi.WifiInfo
import android.os.Build
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import org.robolectric.annotation.Config

@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class WiFiSecurityParameterized1Test(
    val wiFiSecurity: WiFiSecurity,
    val expected: Set<Security>,
) {
    @Test
    fun securities() {
        assertThat(wiFiSecurity.securities).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{index}: {0} -> {1}")
        fun data() =
            listOf(
                arrayOf(WiFiSecurity(), setOf(Security.NONE)),
                arrayOf(
                    WiFiSecurity("WPA-WPA2-WPA-WEP-YZX-WPA3-WPS-WPA2-NONE"),
                    setOf(Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3),
                ),
                arrayOf(
                    WiFiSecurity("WPA-[FT/WPA2]-[WPA]-[WEP-FT/SAE+TST][KPG-WPS-NONE]EAP_SUITE_B_192"),
                    setOf(Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3),
                ),
                arrayOf(WiFiSecurity(securityTypes = WiFiSecurityTypeTest.All), setOf(Security.WEP, Security.WPA3)),
                arrayOf(WiFiSecurity(securityTypes = WiFiSecurityTypeTest.WEP), setOf(Security.WEP)),
                arrayOf(WiFiSecurity(securityTypes = WiFiSecurityTypeTest.WPA3), setOf(Security.WPA3)),
                arrayOf(WiFiSecurity(securityTypes = WiFiSecurityTypeTest.NONE), setOf(Security.NONE)),
                arrayOf(WiFiSecurity("YZX-NONE", WiFiSecurityTypeTest.NONE), setOf(Security.NONE)),
                arrayOf(
                    WiFiSecurity(
                        "WPA-WPA2-WPA-YZX-WPS-NONE",
                        listOf(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE, WifiInfo.SECURITY_TYPE_WEP),
                    ),
                    setOf(Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3),
                ),
            )
    }
}
