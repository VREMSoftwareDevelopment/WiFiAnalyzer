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
package com.vrem.wifianalyzer.vendor.model

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class VendorServiceTest {
    private val vendorName = "CISCO SYSTEMS INC"
    private val macAddress = "00:23:AB:8C:DF:10"
    private val vendorNameInvalid = "XXXXX"
    private val macAddressInvalid = "XX:XX:XX"
    private val vendorSize = 19717
    private val macsSize = 39575
    private val macsCiscoSize = 1232
    private val filterVendor = "1394 "
    private val filterMac = "00:A0:2"
    private val expectedVendorName1 = "1394 TRADE ASSOCIATION"
    private val expectedVendorName2 = "TRANSITIONS RESEARCH CORP"
    private val expectedVendorName3 = "1394 PRINTER WORKING GROUP"
    private val expectedMac1 = "00:00:0C"
    private val expectedMac2 = "FC:FB:FB"
    private val expectedMac3 = "2C:86:D2"

    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val fixture = VendorService(mainActivity.resources)

    @Test
    fun findVendorNameUsingLowerCase() {
        // Act
        val actual = fixture.findVendorName(macAddress.lowercase())
        // Assert
        assertThat(actual).isEqualTo(vendorName)
    }

    @Test
    fun findVendorNameWithInvalidMac() {
        // Act
        val actual = fixture.findVendorName(macAddressInvalid)
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun findVendorNameUsingDefault() {
        // Act
        val actual = fixture.findVendorName()
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun findMacAddresses() {
        // Arrange
        // Act
        val actual = fixture.findMacAddresses(vendorName)
        // Assert
        assertThat(actual).hasSize(macsCiscoSize)
        assertThat(actual[0]).isEqualTo(expectedMac1)
        assertThat(actual[macsCiscoSize - 1]).isEqualTo(expectedMac2)
        assertThat(actual[macsCiscoSize / 2]).isEqualTo(expectedMac3)
    }

    @Test
    fun findMacAddressesUsingLowerCase() {
        // Arrange
        // Act
        val actual = fixture.findMacAddresses(vendorName.lowercase())
        // Assert
        assertThat(actual).hasSize(macsCiscoSize)
        assertThat(actual[0]).isEqualTo(expectedMac1)
        assertThat(actual[macsCiscoSize - 1]).isEqualTo(expectedMac2)
        assertThat(actual[macsCiscoSize / 2]).isEqualTo(expectedMac3)
    }

    @Test
    fun findMacAddressesWithInvalidName() {
        // Act
        val actual = fixture.findMacAddresses(vendorNameInvalid)
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun findMacAddressesWithDefault() {
        // Arrange
        // Act
        val actual = fixture.findMacAddresses()
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun findVendors() {
        // Act
        val actual = fixture.findVendors()
        // Assert
        assertThat(actual).hasSize(vendorSize)
    }

    @Test
    fun findVendorsWithVendorFilter() {
        // Act
        val actual = fixture.findVendors(filterVendor)
        // Assert
        assertThat(actual).hasSize(2)
        assertThat(actual[0]).isEqualTo(expectedVendorName3)
        assertThat(actual[1]).isEqualTo(expectedVendorName1)
    }

    @Test
    fun findVendorsWithVendorFilterUsingLowerCase() {
        // Act
        val actual = fixture.findVendors(filterVendor.lowercase())
        // Assert
        assertThat(actual).hasSize(2)
        assertThat(actual[0]).isEqualTo(expectedVendorName3)
        assertThat(actual[1]).isEqualTo(expectedVendorName1)
    }

    @Test
    fun findVendorsWithMacFilter() {
        // Act
        val actual = fixture.findVendors(filterMac)
        // Assert
        assertThat(actual).hasSize(16)
        assertThat(actual[0]).isEqualTo(expectedVendorName1)
        assertThat(actual[15]).isEqualTo(expectedVendorName2)
    }

    @Test
    fun findVendorsWithMacFilterUsingLowerCase() {
        // Act
        val actual = fixture.findVendors(filterMac.lowercase())
        // Assert
        assertThat(actual).hasSize(16)
        assertThat(actual[0]).isEqualTo(expectedVendorName1)
        assertThat(actual[15]).isEqualTo(expectedVendorName2)
    }

    @Test
    fun findMacs() {
        // Act
        val actual = fixture.findMacs()
        // Assert
        assertThat(actual).hasSize(macsSize)
    }
}
