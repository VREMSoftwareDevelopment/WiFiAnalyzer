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

import android.os.Build
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import org.robolectric.annotation.Config

@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class WiFiSecurityParameterized2Test(val security: Security, val wiFiSecurities: List<WiFiSecurity>) {

    @Test
    fun security() {
        wiFiSecurities.forEach { wiFiSecurity ->
            assertThat(wiFiSecurity.security).describedAs("$wiFiSecurity").isEqualTo(security)
        }
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{index}: {0} -> {1}")
        fun data() = listOf(
            arrayOf(
                Security.NONE, listOf(
                    WiFiSecurity(),
                    WiFiSecurity("xyz"),
                    WiFiSecurity("ESS"),
                    WiFiSecurity(securityTypes = WiFiSecurityTypeTest.NONE),
                    WiFiSecurity("xyz", WiFiSecurityTypeTest.NONE)
                )
            ),
            arrayOf(
                Security.WPS, listOf(
                    WiFiSecurity("WPA3-WPA2+WPA[ESS]WEP[]WPS-NONE"),
                    WiFiSecurity("WPA3-WPA2+WPA[ESS]WEP[]WPS"),
                    WiFiSecurity("WPS", WiFiSecurityTypeTest.All)
                )
            ),
            arrayOf(
                Security.WEP, listOf(
                    WiFiSecurity("WPA3-WPA2+WPA[ESS]WEP[]"),
                    WiFiSecurity(securityTypes = WiFiSecurityTypeTest.WEP),
                    WiFiSecurity("WEP", WiFiSecurityTypeTest.All)
                )
            ),
            arrayOf(
                Security.WPA, listOf(
                    WiFiSecurity("WPA3-WPA2+WPA[ESS]"),
                    WiFiSecurity("WPA", WiFiSecurityTypeTest.NONE + WiFiSecurityTypeTest.WPA3)
                )
            ),
            arrayOf(
                Security.WPA2, listOf(
                    WiFiSecurity("WPA3-WPA2+[ESS]"),
                    WiFiSecurity("WPA2", WiFiSecurityTypeTest.NONE + WiFiSecurityTypeTest.WPA3)
                )
            ),
            arrayOf(
                Security.WPA3, listOf(
                    WiFiSecurity("WPA3+[ESS]"),
                    WiFiSecurity("[FT/SAE]+ESS"),
                    WiFiSecurity("[RSN-EAP_SUITE_B_192-GCMP-56][ESS][MFPR][MFPC]"),
                    WiFiSecurity("[RSN-SAE-CCMP][ESS]"),
                    WiFiSecurity("[RSN-EAP_SUITE_B_192-CCMP][ESS]"),
                    WiFiSecurity("[RSN-OWE-CCMP][ESS]"),
                    WiFiSecurity(securityTypes = WiFiSecurityTypeTest.WPA3),
                    WiFiSecurity("WPA3", WiFiSecurityTypeTest.NONE)
                )
            )
        )
    }
}