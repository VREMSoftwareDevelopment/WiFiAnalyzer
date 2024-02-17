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
import android.net.wifi.ScanResult.InformationElement
import android.net.wifi.WifiInfo
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config
import java.nio.ByteBuffer

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class TransformerTest {
    private val informationElement1 = withInformationElement(RM_ENABLED_CAPABILITIES_IE, byteArrayOf(0x7F, 0, 0, 0, 0))
    private val informationElement2 = withInformationElement(EXTENDED_CAPABILITIES_IE, byteArrayOf(0, 0, 0x7F))
    private val informationElement3 = withInformationElement(MOBILE_DOMAIN_IE, byteArrayOf())
    private val scanResult1 =
        withScanResult(SSID_1, BSSID_1, WiFiWidth.MHZ_160, WiFiStandard.AX, WiFiSecurityTypeTest.All)
    private val scanResult2 = withScanResult(
        SSID_2, BSSID_2, WiFiWidth.MHZ_80, WiFiStandard.AC, WiFiSecurityTypeTest.WPA3, listOf(
            informationElement1,
            informationElement2,
            informationElement3
        )
    )
    private val scanResult3 = withScanResult(SSID_3, BSSID_3, WiFiWidth.MHZ_40, WiFiStandard.N, listOf(), listOf())
    private val cacheResults = withCacheResults()
    private val wifiInfo = withWiFiInfo()
    private val cache: Cache = mock()
    private val fixture = Transformer(cache)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wifiInfo)
        verifyNoMoreInteractions(cache)
    }

    @Test
    fun transformWifiInfo() {
        // setup
        val expected = WiFiConnection(WiFiIdentifier(SSID_1, BSSID_1), IP_ADDRESS, LINK_SPEED)
        doReturn(wifiInfo).whenever(cache).wifiInfo
        // execute
        val actual = fixture.transformWifiInfo()
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(cache).wifiInfo
        verifyWiFiInfo()
    }

    @Test
    fun transformWithNulls() {
        // setup
        doReturn(null).whenever(cache).wifiInfo
        // execute
        val actual = fixture.transformWifiInfo()
        // validate
        assertThat(actual).isEqualTo(WiFiConnection.EMPTY)
        verify(cache).wifiInfo
    }

    @Test
    fun transformWifiInfoNotConnected() {
        // setup
        doReturn(wifiInfo).whenever(cache).wifiInfo
        doReturn(-1).whenever(wifiInfo).networkId
        // execute
        val actual = fixture.transformWifiInfo()
        // validate
        assertThat(actual).isEqualTo(WiFiConnection.EMPTY)
        verify(wifiInfo).networkId
        verify(cache).wifiInfo
    }

    @Test
    fun transformScanResults() {
        // setup
        val fastRoaming = FastRoaming.entries.toList()
        doReturn(cacheResults).whenever(cache).scanResults()
        // execute
        val actual = fixture.transformCacheResults()
        // validate
        assertThat(actual).hasSize(cacheResults.size)
        validateWiFiDetail(SSID_1, BSSID_1, WiFiWidth.MHZ_160, WiFiStandard.AX, actual[0], WiFiSecurityTypeTest.All)
        validateWiFiDetail(
            SSID_2,
            BSSID_2,
            WiFiWidth.MHZ_80,
            WiFiStandard.AC,
            actual[1],
            WiFiSecurityTypeTest.WPA3,
            fastRoaming
        )
        validateWiFiDetail(SSID_3, BSSID_3, WiFiWidth.MHZ_40, WiFiStandard.N, actual[2], listOf())
        verify(cache).scanResults()
    }

    @Test
    fun wiFiData() {
        // setup
        val expectedWiFiConnection = WiFiConnection(WiFiIdentifier(SSID_1, BSSID_1), IP_ADDRESS, LINK_SPEED)
        doReturn(wifiInfo).whenever(cache).wifiInfo
        doReturn(cacheResults).whenever(cache).scanResults()
        // execute
        val actual = fixture.transformToWiFiData()
        // validate
        assertThat(actual.wiFiConnection).isEqualTo(expectedWiFiConnection)
        assertThat(actual.wiFiDetails).hasSize(cacheResults.size)
        verifyWiFiInfo()
        verify(cache).wifiInfo
        verify(cache).scanResults()
    }

    private fun withScanResult(
        ssid: SSID,
        bssid: BSSID,
        wiFiWidth: WiFiWidth,
        wiFiStandard: WiFiStandard,
        securityTypes: List<Int>,
        informationElements: List<InformationElement> = listOf()
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
        doReturn(securityTypes.toIntArray()).whenever(scanResult).securityTypes
        doReturn(informationElements).whenever(scanResult).informationElements
        return scanResult
    }

    private fun withInformationElement(id: Int, bytes: ByteArray): InformationElement =
        mock<InformationElement>().apply {
            doReturn(id).whenever(this).id
            doReturn(0).whenever(this).idExt
            doReturn(ByteBuffer.wrap(bytes).asReadOnlyBuffer()).whenever(this).bytes
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
        ssid: String,
        bssid: String,
        wiFiWidth: WiFiWidth,
        wiFiStandard: WiFiStandard,
        wiFiDetail: WiFiDetail,
        securityTypes: List<Int>,
        fastRoaming: List<FastRoaming> = listOf()
    ) {
        with(wiFiDetail) {
            assertThat(wiFiIdentifier.ssid).isEqualTo(ssid)
            assertThat(wiFiIdentifier.bssid).isEqualTo(bssid)
            assertThat(wiFiSignal.wiFiWidth).isEqualTo(wiFiWidth)
            assertThat(wiFiSignal.extra.wiFiStandard).isEqualTo(wiFiStandard)
            assertThat(wiFiSignal.level).isEqualTo(LEVEL)
            assertThat(wiFiSignal.primaryFrequency).isEqualTo(FREQUENCY)
            assertThat(wiFiSignal.centerFrequency).isEqualTo(FREQUENCY + wiFiWidth.frequencyWidthHalf)
            assertThat(wiFiSignal.extra.fastRoaming).isEqualTo(fastRoaming)
            assertThat(wiFiSecurity.capabilities).isEqualTo(WPA)
            assertThat(wiFiSecurity.securityTypes).isEqualTo(securityTypes)
        }
    }

    private fun withCacheResults() = listOf(
        CacheResult(scanResult1, scanResult1.level),
        CacheResult(scanResult2, scanResult2.level),
        CacheResult(scanResult3, scanResult2.level)
    )

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