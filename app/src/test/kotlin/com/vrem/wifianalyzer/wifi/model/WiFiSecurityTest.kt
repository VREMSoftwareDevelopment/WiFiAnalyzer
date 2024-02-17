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

import android.net.wifi.WifiInfo
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class WiFiSecurityTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity

    @Test
    fun wiFiSecurityTypes() {
        // setup
        val fixture = WiFiSecurity(securityTypes = WiFiSecurityTypeTest.All)
        val expected = WiFiSecurityType.entries.toSet()
        // execute
        val actual: Set<WiFiSecurityType> = fixture.wiFiSecurityTypes
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun wiFiSecurityTypesDisplay() {
        // expected
        val fixture = WiFiSecurity(securityTypes = WiFiSecurityTypeTest.All)
        val expected =
            "[DPP EAP OPEN OSEN PASSPOINT_R1_R2 PASSPOINT_R3 PSK WAPI_CERT WAPI_PSK WEP EAP_WPA3_ENTERPRISE EAP_WPA3_ENTERPRISE_192_BIT OWE SAE]"
        // execute
        val actual = fixture.wiFiSecurityTypesDisplay(mainActivity.applicationContext)
        //validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun securities() {
        // setup
        withSecuritiesValues().forEach { (wiFiSecurity, expected) ->
            println(wiFiSecurity)
            // execute
            val actual: Set<Security> = wiFiSecurity.securities
            // validate
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun security() {
        // setup
        val values: Map<Security, List<WiFiSecurity>> = withSecurityValues()
        Security.entries.forEach { security ->
            println(security)
            values[security]?.forEach { wiFiSecurity ->
                println("\t$wiFiSecurity")
                // execute
                val actual = wiFiSecurity.security
                // validate
                assertThat(actual).isEqualTo(security)
            }
        }
    }

    private fun withSecuritiesValues(): Map<WiFiSecurity, Set<Security>> {
        return mapOf(
            WiFiSecurity() to setOf(Security.NONE),
            WiFiSecurity("WPA-WPA2-WPA-WEP-YZX-WPA3-WPS-WPA2-NONE") to setOf(
                Security.WPS,
                Security.WEP,
                Security.WPA,
                Security.WPA2,
                Security.WPA3
            ),
            WiFiSecurity("WPA-[FT/WPA2]-[WPA]-[WEP-FT/SAE+TST][KPG-WPS-NONE]EAP_SUITE_B_192") to setOf(
                Security.WPS,
                Security.WEP,
                Security.WPA,
                Security.WPA2,
                Security.WPA3
            ),
            WiFiSecurity(securityTypes = WiFiSecurityTypeTest.All) to setOf(Security.WEP, Security.WPA3),
            WiFiSecurity(securityTypes = WiFiSecurityTypeTest.WEP) to setOf(Security.WEP),
            WiFiSecurity(securityTypes = WiFiSecurityTypeTest.WPA3) to setOf(Security.WPA3),
            WiFiSecurity(securityTypes = WiFiSecurityTypeTest.NONE) to setOf(Security.NONE),
            WiFiSecurity("YZX-NONE", WiFiSecurityTypeTest.NONE) to setOf(Security.NONE),
            WiFiSecurity(
                "WPA-WPA2-WPA-YZX-WPS-NONE",
                listOf(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE, WifiInfo.SECURITY_TYPE_WEP)
            ) to setOf(
                Security.WPS,
                Security.WEP,
                Security.WPA,
                Security.WPA2,
                Security.WPA3
            )
        )
    }

    private fun withSecurityValues(): Map<Security, List<WiFiSecurity>> {
        return mapOf(
            Security.NONE to listOf(
                WiFiSecurity(),
                WiFiSecurity("xyz"),
                WiFiSecurity("ESS"),
                WiFiSecurity(securityTypes = WiFiSecurityTypeTest.NONE),
                WiFiSecurity("xyz", WiFiSecurityTypeTest.NONE)
            ),
            Security.WPS to listOf(
                WiFiSecurity("WPA3-WPA2+WPA[ESS]WEP[]WPS-NONE"),
                WiFiSecurity("WPA3-WPA2+WPA[ESS]WEP[]WPS"),
                WiFiSecurity("WPS", WiFiSecurityTypeTest.All)
            ),
            Security.WEP to listOf(
                WiFiSecurity("WPA3-WPA2+WPA[ESS]WEP[]"),
                WiFiSecurity(securityTypes = WiFiSecurityTypeTest.WEP),
                WiFiSecurity("WEP", WiFiSecurityTypeTest.All)
            ),
            Security.WPA to listOf(
                WiFiSecurity("WPA3-WPA2+WPA[ESS]"),
                WiFiSecurity("WPA", WiFiSecurityTypeTest.NONE + WiFiSecurityTypeTest.WPA3)
            ),
            Security.WPA2 to listOf(
                WiFiSecurity("WPA3-WPA2+[ESS]"),
                WiFiSecurity("WPA2", WiFiSecurityTypeTest.NONE + WiFiSecurityTypeTest.WPA3)
            ),
            Security.WPA3 to listOf(
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
    }

}