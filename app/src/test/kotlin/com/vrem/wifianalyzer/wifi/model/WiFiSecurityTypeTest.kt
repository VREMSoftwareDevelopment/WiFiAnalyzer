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

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class WiFiSecurityTypeTest {

    @Test
    fun size() {
        assertThat(WiFiSecurityType.entries).hasSize(All.size)
    }

    @Test
    fun findOneWithSecurityTypes() {
        All.forEach {
            println(it)
            // execute & validate
            assertThat(WiFiSecurityType.findOne(it)).isNotNull()
        }
    }

    @Test
    fun textResource() {
        assertThat(WiFiSecurityType.UNKNOWN.textResource).isEqualTo(R.string.security_type_unknown)
        assertThat(WiFiSecurityType.OPEN.textResource).isEqualTo(R.string.security_type_open)
        assertThat(WiFiSecurityType.WEP.textResource).isEqualTo(R.string.security_type_wep)
        assertThat(WiFiSecurityType.PSK.textResource).isEqualTo(R.string.security_type_psk)
        assertThat(WiFiSecurityType.EAP.textResource).isEqualTo(R.string.security_type_eap)
        assertThat(WiFiSecurityType.SAE.textResource).isEqualTo(R.string.security_type_sae)
        assertThat(WiFiSecurityType.EAP_WPA3_ENTERPRISE_192_BIT.textResource).isEqualTo(R.string.security_type_eap_wpa3_enterprise_192_bit)
        assertThat(WiFiSecurityType.OWE.textResource).isEqualTo(R.string.security_type_owe)
        assertThat(WiFiSecurityType.WAPI_PSK.textResource).isEqualTo(R.string.security_type_wapi_psk)
        assertThat(WiFiSecurityType.WAPI_CERT.textResource).isEqualTo(R.string.security_type_wapi_cert)
        assertThat(WiFiSecurityType.EAP_WPA3_ENTERPRISE.textResource).isEqualTo(R.string.security_type_eap_wpa3_enterprise)
        assertThat(WiFiSecurityType.OSEN.textResource).isEqualTo(R.string.security_type_osen)
        assertThat(WiFiSecurityType.PASSPOINT_R1_R2.textResource).isEqualTo(R.string.security_type_passpoint_r1_r2)
        assertThat(WiFiSecurityType.PASSPOINT_R3.textResource).isEqualTo(R.string.security_type_passpoint_r3)
        assertThat(WiFiSecurityType.SECURITY_TYPE_DPP.textResource).isEqualTo(R.string.security_type_dpp)
    }

    @Test
    fun security() {
        assertThat(WiFiSecurityType.UNKNOWN.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.OPEN.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.WEP.security).isEqualTo(Security.WEP)
        assertThat(WiFiSecurityType.PSK.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.EAP.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.SAE.security).isEqualTo(Security.WPA3)
        assertThat(WiFiSecurityType.EAP_WPA3_ENTERPRISE_192_BIT.security).isEqualTo(Security.WPA3)
        assertThat(WiFiSecurityType.OWE.security).isEqualTo(Security.WPA3)
        assertThat(WiFiSecurityType.WAPI_PSK.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.WAPI_CERT.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.EAP_WPA3_ENTERPRISE.security).isEqualTo(Security.WPA3)
        assertThat(WiFiSecurityType.OSEN.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.PASSPOINT_R1_R2.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.PASSPOINT_R3.security).isEqualTo(Security.NONE)
        assertThat(WiFiSecurityType.SECURITY_TYPE_DPP.security).isEqualTo(Security.NONE)
    }

    @Test
    fun wiFIStandard() {
        assertThat(WiFiSecurityType.UNKNOWN.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_UNKNOWN)
        assertThat(WiFiSecurityType.OPEN.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_OPEN)
        assertThat(WiFiSecurityType.WEP.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_WEP)
        assertThat(WiFiSecurityType.PSK.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_PSK)
        assertThat(WiFiSecurityType.EAP.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_EAP)
        assertThat(WiFiSecurityType.SAE.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_SAE)
        assertThat(WiFiSecurityType.EAP_WPA3_ENTERPRISE_192_BIT.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT)
        assertThat(WiFiSecurityType.OWE.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_OWE)
        assertThat(WiFiSecurityType.WAPI_PSK.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_WAPI_PSK)
        assertThat(WiFiSecurityType.WAPI_CERT.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_WAPI_CERT)
        assertThat(WiFiSecurityType.EAP_WPA3_ENTERPRISE.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE)
        assertThat(WiFiSecurityType.OSEN.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_OSEN)
        assertThat(WiFiSecurityType.PASSPOINT_R1_R2.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_PASSPOINT_R1_R2)
        assertThat(WiFiSecurityType.PASSPOINT_R3.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_PASSPOINT_R3)
        assertThat(WiFiSecurityType.SECURITY_TYPE_DPP.securityTypeId).isEqualTo(WifiInfo.SECURITY_TYPE_DPP)
    }

    @Test
    fun findOne() {
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_UNKNOWN)).isEqualTo(WiFiSecurityType.UNKNOWN)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_OPEN)).isEqualTo(WiFiSecurityType.OPEN)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_WEP)).isEqualTo(WiFiSecurityType.WEP)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_PSK)).isEqualTo(WiFiSecurityType.PSK)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_EAP)).isEqualTo(WiFiSecurityType.EAP)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_SAE)).isEqualTo(WiFiSecurityType.SAE)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT)).isEqualTo(WiFiSecurityType.EAP_WPA3_ENTERPRISE_192_BIT)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_OWE)).isEqualTo(WiFiSecurityType.OWE)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_WAPI_PSK)).isEqualTo(WiFiSecurityType.WAPI_PSK)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_WAPI_CERT)).isEqualTo(WiFiSecurityType.WAPI_CERT)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE)).isEqualTo(WiFiSecurityType.EAP_WPA3_ENTERPRISE)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_OSEN)).isEqualTo(WiFiSecurityType.OSEN)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_PASSPOINT_R1_R2)).isEqualTo(WiFiSecurityType.PASSPOINT_R1_R2)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_PASSPOINT_R3)).isEqualTo(WiFiSecurityType.PASSPOINT_R3)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_DPP)).isEqualTo(WiFiSecurityType.SECURITY_TYPE_DPP)

        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_UNKNOWN - 1)).isEqualTo(WiFiSecurityType.UNKNOWN)
        assertThat(WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_DPP + 1)).isEqualTo(WiFiSecurityType.UNKNOWN)
    }

    @Test
    fun findAll() {
        // setup
        val securityTypes = All
        val expected = WiFiSecurityType.entries.toSet()
        // execute
        val actual = WiFiSecurityType.findAll(securityTypes)
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Config(sdk = [Build.VERSION_CODES.S_V2])
    @Test
    fun findLegacy() {
        // setup
        val scanResult: ScanResult = mock()
        // execute
        val actual = WiFiSecurityType.find(scanResult)
        // validate
        assertThat(actual).isEmpty()
        verifyNoMoreInteractions(scanResult)
    }

    @Test
    fun find() {
        // setup
        val scanResult: ScanResult = mock()
        val expected: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        doReturn(expected.toIntArray()).whenever(scanResult).securityTypes
        // execute
        val actual = WiFiSecurityType.find(scanResult)
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(scanResult).securityTypes
        verifyNoMoreInteractions(scanResult)
    }

    companion object {
        internal val NONE = listOf(
            WifiInfo.SECURITY_TYPE_DPP,
            WifiInfo.SECURITY_TYPE_EAP,
            WifiInfo.SECURITY_TYPE_OPEN,
            WifiInfo.SECURITY_TYPE_OSEN,
            WifiInfo.SECURITY_TYPE_PASSPOINT_R1_R2,
            WifiInfo.SECURITY_TYPE_PASSPOINT_R3,
            WifiInfo.SECURITY_TYPE_PSK,
            WifiInfo.SECURITY_TYPE_UNKNOWN,
            WifiInfo.SECURITY_TYPE_WAPI_CERT,
            WifiInfo.SECURITY_TYPE_WAPI_PSK,
        )

        internal val WPA3 = listOf(
            WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE,
            WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT,
            WifiInfo.SECURITY_TYPE_OWE,
            WifiInfo.SECURITY_TYPE_SAE,
        )

        internal val WEP = listOf(WifiInfo.SECURITY_TYPE_WEP)

        internal val All = NONE + WEP + WPA3
    }

}