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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import java.nio.ByteBuffer.wrap

@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class FastRoamingParameterizedTest(
    val expected: List<FastRoaming>,
    val elements: List<Element>,
) {
    @Test
    fun find() {
        // setup
        val scanResult: ScanResult = mock()
        val informationElements =
            elements
                .map {
                    val informationElement = mock<InformationElement>()
                    val bytes = wrap(it.bytes.toByteArray()).asReadOnlyBuffer()
                    doReturn(it.id).whenever(informationElement).id
                    doReturn(bytes).whenever(informationElement).bytes
                    informationElement
                }.toList()
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

    data class Element(
        val id: Int,
        val bytes: List<Byte>,
        val countId: Int = 3,
        val countBytes: Int = 3,
    )

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{index}: {0} | {1}")
        fun data() =
            listOf(
                arrayOf(
                    listOf(FastRoaming.FR_802_11K),
                    listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0x7F, 0, 0, 0, 0))),
                ),
                arrayOf(
                    listOf(FastRoaming.FR_802_11R),
                    listOf(Element(MOBILE_DOMAIN_IE, listOf(0))),
                ),
                arrayOf(
                    listOf(FastRoaming.FR_802_11V),
                    listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0, 0, 0x7F))),
                ),
                arrayOf(
                    FastRoaming.entries.toList(),
                    listOf(
                        Element(RM_ENABLED_CAPABILITIES_IE, listOf(0x7F, 0, 0, 0, 0), 3, 3),
                        Element(MOBILE_DOMAIN_IE, listOf(0), 2, 2),
                        Element(EXTENDED_CAPABILITIES_IE, listOf(0, 0, 0x7F), 1, 1),
                    ),
                ),
                arrayOf(listOf(), listOf()),
                arrayOf(listOf(), listOf(Element(0, listOf()))),
                arrayOf(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0)))),
                arrayOf(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F)))),
                arrayOf(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F, 0x7F)))),
                arrayOf(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F, 0x7F, 0x7F)))),
                arrayOf(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F, 0x7F, 0x7F, 0x7F)))),
                arrayOf(listOf(), listOf(Element(RM_ENABLED_CAPABILITIES_IE, listOf(0, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F)))),
                arrayOf(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0)))),
                arrayOf(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F)))),
                arrayOf(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F, 0x7F)))),
                arrayOf(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F, 0x7F, 0)))),
                arrayOf(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F, 0x7F, 0, 0x7F)))),
                arrayOf(listOf(), listOf(Element(EXTENDED_CAPABILITIES_IE, listOf(0x7F, 0x7F, 0, 0x7F, 0x7F)))),
            )
    }
}
