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
package com.vrem.wifianalyzer.wifi.model

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
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
        // validate
        assertThat(actual).isEqualTo(expected)
    }
}
