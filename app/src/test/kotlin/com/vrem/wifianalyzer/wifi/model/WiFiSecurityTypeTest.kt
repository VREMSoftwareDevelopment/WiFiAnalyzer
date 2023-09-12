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

import android.net.wifi.WifiInfo
import com.vrem.wifianalyzer.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WiFiSecurityTypeTest {

    @Test
    fun testSize() {
        assertEquals(All.size, WiFiSecurityType.entries.size)
    }

    @Test
    fun testFindOneWithSecurityTypes() {
        All.forEach {
            println(it)
            // execute & validate
            assertNotNull(WiFiSecurityType.findOne(it))
        }
    }

    @Test
    fun testTextResource() {
        assertEquals(R.string.security_type_unknown, WiFiSecurityType.UNKNOWN.textResource)
        assertEquals(R.string.security_type_open, WiFiSecurityType.OPEN.textResource)
        assertEquals(R.string.security_type_wep, WiFiSecurityType.WEP.textResource)
        assertEquals(R.string.security_type_psk, WiFiSecurityType.PSK.textResource)
        assertEquals(R.string.security_type_eap, WiFiSecurityType.EAP.textResource)
        assertEquals(R.string.security_type_sae, WiFiSecurityType.SAE.textResource)
        assertEquals(R.string.security_type_eap_wpa3_enterprise_192_bit, WiFiSecurityType.EAP_WPA3_ENTERPRISE_192_BIT.textResource)
        assertEquals(R.string.security_type_owe, WiFiSecurityType.OWE.textResource)
        assertEquals(R.string.security_type_wapi_psk, WiFiSecurityType.WAPI_PSK.textResource)
        assertEquals(R.string.security_type_wapi_cert, WiFiSecurityType.WAPI_CERT.textResource)
        assertEquals(R.string.security_type_eap_wpa3_enterprise, WiFiSecurityType.EAP_WPA3_ENTERPRISE.textResource)
        assertEquals(R.string.security_type_osen, WiFiSecurityType.OSEN.textResource)
        assertEquals(R.string.security_type_passpoint_r1_r2, WiFiSecurityType.PASSPOINT_R1_R2.textResource)
        assertEquals(R.string.security_type_passpoint_r3, WiFiSecurityType.PASSPOINT_R3.textResource)
        assertEquals(R.string.security_type_dpp, WiFiSecurityType.SECURITY_TYPE_DPP.textResource)
    }

    @Test
    fun testSecurity() {
        assertEquals(Security.NONE, WiFiSecurityType.UNKNOWN.security)
        assertEquals(Security.NONE, WiFiSecurityType.OPEN.security)
        assertEquals(Security.WEP, WiFiSecurityType.WEP.security)
        assertEquals(Security.NONE, WiFiSecurityType.PSK.security)
        assertEquals(Security.NONE, WiFiSecurityType.EAP.security)
        assertEquals(Security.WPA3, WiFiSecurityType.SAE.security)
        assertEquals(Security.WPA3, WiFiSecurityType.EAP_WPA3_ENTERPRISE_192_BIT.security)
        assertEquals(Security.WPA3, WiFiSecurityType.OWE.security)
        assertEquals(Security.NONE, WiFiSecurityType.WAPI_PSK.security)
        assertEquals(Security.NONE, WiFiSecurityType.WAPI_CERT.security)
        assertEquals(Security.WPA3, WiFiSecurityType.EAP_WPA3_ENTERPRISE.security)
        assertEquals(Security.NONE, WiFiSecurityType.OSEN.security)
        assertEquals(Security.NONE, WiFiSecurityType.PASSPOINT_R1_R2.security)
        assertEquals(Security.NONE, WiFiSecurityType.PASSPOINT_R3.security)
        assertEquals(Security.NONE, WiFiSecurityType.SECURITY_TYPE_DPP.security)
    }

    @Test
    fun testWiFIStandard() {
        assertEquals(WifiInfo.SECURITY_TYPE_UNKNOWN, WiFiSecurityType.UNKNOWN.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_OPEN, WiFiSecurityType.OPEN.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_WEP, WiFiSecurityType.WEP.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_PSK, WiFiSecurityType.PSK.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_EAP, WiFiSecurityType.EAP.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_SAE, WiFiSecurityType.SAE.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT, WiFiSecurityType.EAP_WPA3_ENTERPRISE_192_BIT.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_OWE, WiFiSecurityType.OWE.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_WAPI_PSK, WiFiSecurityType.WAPI_PSK.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_WAPI_CERT, WiFiSecurityType.WAPI_CERT.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE, WiFiSecurityType.EAP_WPA3_ENTERPRISE.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_OSEN, WiFiSecurityType.OSEN.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_PASSPOINT_R1_R2, WiFiSecurityType.PASSPOINT_R1_R2.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_PASSPOINT_R3, WiFiSecurityType.PASSPOINT_R3.securityTypeId)
        assertEquals(WifiInfo.SECURITY_TYPE_DPP, WiFiSecurityType.SECURITY_TYPE_DPP.securityTypeId)
    }

    @Test
    fun testFindOne() {
        assertEquals(WiFiSecurityType.UNKNOWN, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_UNKNOWN))
        assertEquals(WiFiSecurityType.OPEN, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_OPEN))
        assertEquals(WiFiSecurityType.WEP, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_WEP))
        assertEquals(WiFiSecurityType.PSK, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_PSK))
        assertEquals(WiFiSecurityType.EAP, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_EAP))
        assertEquals(WiFiSecurityType.SAE, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_SAE))
        assertEquals(
            WiFiSecurityType.EAP_WPA3_ENTERPRISE_192_BIT,
            WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT)
        )
        assertEquals(WiFiSecurityType.OWE, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_OWE))
        assertEquals(WiFiSecurityType.WAPI_PSK, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_WAPI_PSK))
        assertEquals(WiFiSecurityType.WAPI_CERT, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_WAPI_CERT))
        assertEquals(WiFiSecurityType.EAP_WPA3_ENTERPRISE, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE))
        assertEquals(WiFiSecurityType.OSEN, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_OSEN))
        assertEquals(WiFiSecurityType.PASSPOINT_R1_R2, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_PASSPOINT_R1_R2))
        assertEquals(WiFiSecurityType.PASSPOINT_R3, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_PASSPOINT_R3))
        assertEquals(WiFiSecurityType.SECURITY_TYPE_DPP, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_DPP))

        assertEquals(WiFiSecurityType.UNKNOWN, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_UNKNOWN - 1))
        assertEquals(WiFiSecurityType.UNKNOWN, WiFiSecurityType.findOne(WifiInfo.SECURITY_TYPE_DPP + 1))
    }

    @Test
    fun testFindAll() {
        // setup
        val securityTypes = All
        val expected = WiFiSecurityType.entries.toSet()
        // execute
        val actual = WiFiSecurityType.findAll(securityTypes)
        // validate
        assertEquals(expected, actual)
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