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
package com.vrem.wifianalyzer.wifi.scanner

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.wifi.model.*
import com.vrem.wifianalyzer.wifi.model.WiFiSignal.Companion.EXTENDED_CAPABILITIES_IE
import com.vrem.wifianalyzer.wifi.model.WiFiSignal.Companion.MOBILE_DOMAIN_IE
import com.vrem.wifianalyzer.wifi.model.WiFiSignal.Companion.RM_ENABLED_CAPABILITIES_IE

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.ByteBuffer

class TransformerTest {
    private val withoutFastRoaming = listOf(
        mockInformationElement(0, 0, byteArrayOf()),
        mockInformationElement(RM_ENABLED_CAPABILITIES_IE, 0, ByteArray(5)),
        mockInformationElement(EXTENDED_CAPABILITIES_IE, 0, ByteArray(3)),
    )
    private val withFastRoaming = listOf(
        mockInformationElement(RM_ENABLED_CAPABILITIES_IE, 0, byteArrayOf(0x7F, 0, 0, 0, 0)),
        mockInformationElement(EXTENDED_CAPABILITIES_IE, 0, byteArrayOf(0, 0, 0x7F)),
        mockInformationElement(MOBILE_DOMAIN_IE, 0, ByteArray(5)),
    )
    private val scanResult1 = withScanResult(SSID_1, BSSID_1, WiFiWidth.MHZ_160, WiFiStandard.AX, WiFiSecurityTypeTest.All)
    private val scanResult2 = withScanResult(SSID_2, BSSID_2, WiFiWidth.MHZ_80, WiFiStandard.AC, WiFiSecurityTypeTest.WPA3)
    private val scanResult3 = withScanResult(SSID_3, BSSID_3, WiFiWidth.MHZ_40, WiFiStandard.N, listOf())
    private val cacheResults = listOf(
        CacheResult(scanResult1, scanResult1.level),
        CacheResult(scanResult2, scanResult2.level),
        CacheResult(scanResult3, scanResult2.level)
    )
    private val wifiInfo = withWiFiInfo()
    private val cache: Cache = mock()
    private val fixture = spy(Transformer(cache))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wifiInfo)
        verifyNoMoreInteractions(cache)
    }

    @Test
    fun testTransformWifiInfo() {
        // setup
        val expected = WiFiConnection(WiFiIdentifier(SSID_1, BSSID_1), IP_ADDRESS, LINK_SPEED)
        doReturn(wifiInfo).whenever(cache).wifiInfo
        // execute
        val actual = fixture.transformWifiInfo()
        // validate
        assertEquals(expected, actual)
        verify(cache).wifiInfo
        verifyWiFiInfo()
    }

    @Test
    fun testTransformWithNulls() {
        // setup
        doReturn(null).whenever(cache).wifiInfo
        // execute
        val actual = fixture.transformWifiInfo()
        // validate
        assertEquals(WiFiConnection.EMPTY, actual)
        verify(cache).wifiInfo
    }

    @Test
    fun testTransformWifiInfoNotConnected() {
        // setup
        doReturn(wifiInfo).whenever(cache).wifiInfo
        doReturn(-1).whenever(wifiInfo).networkId
        // execute
        val actual = fixture.transformWifiInfo()
        // validate
        assertEquals(WiFiConnection.EMPTY, actual)
        verify(wifiInfo).networkId
        verify(cache).wifiInfo
    }

    @Test
    fun testTransformScanResults() {
        // setup
        doReturn(true).whenever(fixture).minVersionR()
        doReturn(true).whenever(fixture).minVersionT()
        doReturn(cacheResults).whenever(cache).scanResults()
        // execute
        val actual = fixture.transformCacheResults()
        // validate
        assertEquals(cacheResults.size, actual.size)
        validateWiFiDetail(SSID_1, BSSID_1, WiFiWidth.MHZ_160, WiFiStandard.AX, actual[0], WiFiSecurityTypeTest.All)
        validateWiFiDetail(SSID_2, BSSID_2, WiFiWidth.MHZ_80, WiFiStandard.AC, actual[1], WiFiSecurityTypeTest.WPA3)
        validateWiFiDetail(SSID_3, BSSID_3, WiFiWidth.MHZ_40, WiFiStandard.N, actual[2], listOf())
        verify(fixture, times(6)).minVersionR()
        verify(fixture, times(3)).minVersionT()
        verify(cache).scanResults()
    }

    @Test
    fun testWiFiData() {
        // setup
        val expectedWiFiConnection = WiFiConnection(WiFiIdentifier(SSID_1, BSSID_1), IP_ADDRESS, LINK_SPEED)
        doReturn(wifiInfo).whenever(cache).wifiInfo
        doReturn(cacheResults).whenever(cache).scanResults()
        // execute
        val actual = fixture.transformToWiFiData()
        // validate
        assertEquals(expectedWiFiConnection, actual.wiFiConnection)
        assertEquals(cacheResults.size, actual.wiFiDetails.size)
        verifyWiFiInfo()
        verify(cache).wifiInfo
        verify(cache).scanResults()
    }

    @Test
    fun testWiFiStandardMinVersionR() {
        // setup
        doReturn(true).whenever(fixture).minVersionR()
        // execute
        val actual = fixture.wiFiStandard(scanResult1)
        // validate
        assertEquals(WiFiStandard.AX.wiFiStandardId, actual)
        verify(fixture).minVersionR()
    }

    @Test
    fun testWiFiStandard() {
        // setup
        doReturn(false).whenever(fixture).minVersionR()
        // execute
        val actual = fixture.wiFiStandard(scanResult1)
        // validate
        assertEquals(WiFiStandard.UNKNOWN.wiFiStandardId, actual)
        verify(fixture).minVersionR()
    }

    @Test
    fun testSecurityTypesMinVersionT() {
        // setup
        doReturn(true).whenever(fixture).minVersionT()
        // execute
        val actual = fixture.securityTypes(scanResult1)
        // validate
        assertEquals(WiFiSecurityTypeTest.All, actual)
        verify(fixture).minVersionT()
    }

    @Test
    fun testSecurityTypes() {
        // setup
        doReturn(false).whenever(fixture).minVersionT()
        // execute
        val actual = fixture.securityTypes(scanResult1)
        // validate
        assertTrue(actual.isEmpty())
        verify(fixture).minVersionT()
    }

    @Test
    fun testFastRoamingMinVersionR() {
        // setup
        doReturn(false).whenever(fixture).minVersionR()
        // execute
        val actual = fixture.fastRoaming(scanResult1)
        // validate
        assertEquals(listOf(FastRoaming.REQUIRE_ANDROID_R),  actual)
        verify(fixture).minVersionR()
    }

    @Test
    fun testFastRoaming() {
        // setup
        doReturn(true).whenever(fixture).minVersionR()
        doReturn(withFastRoaming).whenever(scanResult1).informationElements

        // execute
        val actual = fixture.fastRoaming(scanResult1)
        // validate
        assertEquals(listOf(FastRoaming.K, FastRoaming.V, FastRoaming.R),  actual)
        verify(fixture).minVersionR()
    }

    @Test
    fun testNoFastRoaming() {
        // setup
        doReturn(true).whenever(fixture).minVersionR()
        doReturn(withoutFastRoaming).whenever(scanResult1).informationElements
        // execute
        val actual = fixture.fastRoaming(scanResult1)
        // validate
        assertEquals(listOf<FastRoaming>(),  actual)
        verify(fixture).minVersionR()
    }

    private fun withScanResult(
        ssid: SSID,
        bssid: BSSID,
        wiFiWidth: WiFiWidth,
        wiFiStandard: WiFiStandard,
        securityTypes: List<Int>?
    ): ScanResult {
        val scanResult: ScanResult = mock()
        whenSsid(scanResult, ssid)
        scanResult.BSSID = bssid
        scanResult.capabilities = WPA
        scanResult.frequency = FREQUENCY
        scanResult.centerFreq0 = when (wiFiWidth) {
            WiFiWidth.MHZ_20 -> FREQUENCY
            WiFiWidth.MHZ_40 -> FREQUENCY + wiFiWidth.frequencyWidth
            WiFiWidth.MHZ_80 -> FREQUENCY + wiFiWidth.frequencyWidthHalf
            WiFiWidth.MHZ_160 -> FREQUENCY + wiFiWidth.frequencyWidth
            WiFiWidth.MHZ_80_PLUS -> FREQUENCY + wiFiWidth.frequencyWidthHalf
        }
        scanResult.level = LEVEL
        scanResult.channelWidth = wiFiWidth.ordinal
        doReturn(wiFiStandard.wiFiStandardId).whenever(scanResult).wifiStandard
        doReturn(securityTypes?.toIntArray()).whenever(scanResult).securityTypes
        return scanResult
    }

    private fun mockInformationElement(id: Int, idExt: Int, bytes: ByteArray)
            : ScanResult.InformationElement {
        return mock<ScanResult.InformationElement>().apply {
            doReturn(id).whenever(this).id
            doReturn(idExt).whenever(this).idExt
            doReturn(ByteBuffer.wrap(bytes).asReadOnlyBuffer()).whenever(this).bytes
        }
    }

    private fun withWiFiInfo(): WifiInfo {
        val wifiInfo: WifiInfo = mock()
        doReturn(0).whenever(wifiInfo).networkId
        doReturn(SSID_1).whenever(wifiInfo).ssid
        doReturn(BSSID_1).whenever(wifiInfo).bssid
        doReturn(IP_ADDRESS_VALUE).whenever(wifiInfo).ipV4Address()
        doReturn(LINK_SPEED).whenever(wifiInfo).linkSpeed
        return wifiInfo
    }

    private fun verifyWiFiInfo() {
        verify(wifiInfo).networkId
        verify(wifiInfo).ssid
        verify(wifiInfo).bssid
        verify(wifiInfo).ipV4Address()
        verify(wifiInfo).linkSpeed
    }

    private fun validateWiFiDetail(
        ssid: String, bssid: String, wiFiWidth: WiFiWidth, wiFiStandard: WiFiStandard, wiFiDetail: WiFiDetail, securityTypes: List<Int>
    ) {
        with(wiFiDetail) {
            assertEquals(ssid, wiFiIdentifier.ssid)
            assertEquals(bssid, wiFiIdentifier.bssid)
            assertEquals(wiFiWidth, wiFiSignal.wiFiWidth)
            assertEquals(wiFiStandard, wiFiSignal.wiFiStandard)
            assertEquals(LEVEL, wiFiSignal.level)
            assertEquals(FREQUENCY, wiFiSignal.primaryFrequency)
            assertEquals(FREQUENCY + wiFiWidth.frequencyWidthHalf, wiFiSignal.centerFrequency)
            assertEquals(WPA, wiFiSecurity.capabilities)
            assertEquals(securityTypes, wiFiSecurity.securityTypes)
        }
    }

    companion object {
        private const val SSID_1 = "SSID_1-123"
        private const val BSSID_1 = "BSSID_1-123"
        private const val SSID_2 = "SSID_2-123"
        private const val BSSID_2 = "BSSID_2-123"
        private const val SSID_3 = "SSID_3-123"
        private const val BSSID_3 = "BSSID_3-123"
        private const val WPA = "WPA"
        private const val FREQUENCY = 5170
        private const val LEVEL = -40
        private const val IP_ADDRESS_VALUE = 123456789
        private const val IP_ADDRESS = "21.205.91.7"
        private const val LINK_SPEED = 21
    }
}