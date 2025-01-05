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

import android.net.wifi.ScanResult
import android.net.wifi.ScanResult.InformationElement
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config
import java.nio.ByteBuffer

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class FastRoamingTest {
    @Config(sdk = [Build.VERSION_CODES.Q])
    @Test
    fun findLegacy() {
        // setup
        val scanResult: ScanResult = mock()
        // execute
        val actual = FastRoaming.find(scanResult)
        // validate
        assertThat(actual).isEmpty()
        verifyNoMoreInteractions(scanResult)
    }

    @Test
    fun find() {
        withTestDatas().forEach {
            println(it)
            validate(it.expected, it.elements)
        }
    }

    private fun validate(expected: List<FastRoaming>, elements: List<Element>) {
        // setup
        val scanResult: ScanResult = mock()
        val informationElements = elements
            .map {
                val informationElement = mock<InformationElement>()
                val bytes = ByteBuffer.wrap(it.bytes.toByteArray()).asReadOnlyBuffer()
                doReturn(it.id).whenever(informationElement).id
                doReturn(bytes).whenever(informationElement).bytes
                informationElement
            }
            .toList()
        doReturn(informationElements).whenever(scanResult).informationElements
        // execute
        val actual = FastRoaming.find(scanResult)
        // validate
        assertThat(actual).isEqualTo(expected)
        informationElements.forEachIndexed { index, it ->
            verify(it, times(elements[index].countId)).id
            verify(it, times(elements[index].countBytes)).bytes
        }
        verify(scanResult).informationElements
        verifyNoMoreInteractions(scanResult)
    }

    private data class Element(val id: Int, val bytes: List<Byte>, val countId: Int = 3, val countBytes: Int = 3)

    private data class TestData(val expected: List<FastRoaming>, val elements: List<Element>)

    private fun withTestDatas() =
        listOf(
            TestData(
                listOf(FastRoaming.FR_802_11K),
                listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0x7F, 0, 0, 0, 0)))
            ),
            TestData(
                listOf(FastRoaming.FR_802_11R),
                listOf(Element(MOBILE_DOMAIN_IE, listOf(0)))
            ),
            TestData(
                listOf(FastRoaming.FR_802_11V),
                listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0, 0, 0x7F))),
            ),
            TestData(
                FastRoaming.entries.toList(),
                listOf(
                    Element(RM_ENABLED_CAPABILITIES_IE, listOf(0x7F, 0, 0, 0, 0), 3, 3),
                    Element(MOBILE_DOMAIN_IE, listOf(0), 2, 2),
                    Element(EXTENDED_CAPABILITIES_IE, listOf(0, 0, 0x7F), 1, 1)
                )
            ),
            TestData(listOf(), listOf()),
            TestData(listOf(), listOf(Element(0, listOf()))),
            TestData(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0)))),
            TestData(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F)))),
            TestData(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F, 0x7F)))),
            TestData(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F, 0x7F, 0x7F)))),
            TestData(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F, 0x7F, 0x7F, 0x7F)))),
            TestData(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F)))),
            TestData(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0)))),
            TestData(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F)))),
            TestData(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F, 0x7F)))),
            TestData(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F, 0x7F, 0)))),
            TestData(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F, 0x7F, 0, 0x7F)))),
            TestData(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F, 0x7F, 0, 0x7F, 0x7F))))
        )

}