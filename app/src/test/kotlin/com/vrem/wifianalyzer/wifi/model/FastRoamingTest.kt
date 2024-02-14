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
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.ByteBuffer

class FastRoamingTest {

    private val withoutFastRoaming = listOf(
        mockInformationElement(0, 0, byteArrayOf()),
        mockInformationElement(WiFiSignal.RM_ENABLED_CAPABILITIES_IE, 0, ByteArray(0)),
        mockInformationElement(WiFiSignal.EXTENDED_CAPABILITIES_IE, 0, ByteArray(0)),
        mockInformationElement(WiFiSignal.RM_ENABLED_CAPABILITIES_IE, 0, ByteArray(5)),
        mockInformationElement(WiFiSignal.EXTENDED_CAPABILITIES_IE, 0, ByteArray(3)),
    )
    private val withFastRoaming = listOf(
        mockInformationElement(
            WiFiSignal.RM_ENABLED_CAPABILITIES_IE,
            0,
            byteArrayOf(0x7F, 0, 0, 0, 0)
        ),
        mockInformationElement(WiFiSignal.EXTENDED_CAPABILITIES_IE, 0, byteArrayOf(0, 0, 0x7F)),
        mockInformationElement(WiFiSignal.MOBILE_DOMAIN_IE, 0, ByteArray(5)),
    )

    @Test
    fun testFastRoaming() {
        // execute
        val actual = withFastRoaming.map { FastRoaming.transform(it) }
        // validate
        assertEquals(listOf(FastRoaming.K, FastRoaming.V, FastRoaming.R), actual)
    }

    @Test
    fun testNoFastRoaming() {
        // execute
        val actual = withoutFastRoaming.map { FastRoaming.transform(it) }
        // validate
        assertEquals(List(5) { FastRoaming.ILLEGAL_ATTR }, actual)
    }

    private fun mockInformationElement(id: Int, idExt: Int, bytes: ByteArray)
            : ScanResult.InformationElement {
        return mock<ScanResult.InformationElement>().apply {
            doReturn(id).whenever(this).id
            doReturn(idExt).whenever(this).idExt
            doReturn(ByteBuffer.wrap(bytes).asReadOnlyBuffer()).whenever(this).bytes
        }
    }

}