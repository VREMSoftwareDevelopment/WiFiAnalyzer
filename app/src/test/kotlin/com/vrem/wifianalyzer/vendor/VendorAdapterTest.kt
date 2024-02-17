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
package com.vrem.wifianalyzer.vendor

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.vendor.model.VendorService
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class VendorAdapterTest {
    private val vendorName1 = "N1"
    private val vendorName2 = "N2"
    private val vendorName3 = "N3"

    private lateinit var fixture: VendorAdapter

    private val mainActivity: MainActivity = RobolectricUtil.INSTANCE.activity
    private val vendorService: VendorService = INSTANCE.vendorService
    private val vendors: List<String> = listOf(vendorName1, vendorName2, vendorName3)
    private val macs: List<String> = listOf("MAC1", "MAC2", "MAC3")

    @Before
    fun setUp() {
        whenever(vendorService.findVendors()).thenReturn(vendors)
        fixture = VendorAdapter(mainActivity, vendorService)
    }

    @After
    fun tearDown() {
        verify(vendorService, atLeastOnce()).findVendors()
        verifyNoMoreInteractions(vendorService)
        INSTANCE.restore()
    }

    @Test
    fun constructor() {
        // validate
        assertThat(fixture.count).isEqualTo(vendors.size)
        assertThat(fixture.getItem(0)).isEqualTo(vendors[0])
        assertThat(fixture.getItem(1)).isEqualTo(vendors[1])
        assertThat(fixture.getItem(2)).isEqualTo(vendors[2])
        verify(vendorService).findVendors()
    }

    @Test
    fun getView() {
        // setup
        val expected = macs.joinToString(separator = ", ")
        val viewGroup = mainActivity.findViewById<ViewGroup>(android.R.id.content)
        whenever(vendorService.findMacAddresses(vendorName2)).thenReturn(macs)
        // execute
        val actual = fixture.getView(1, null, viewGroup)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.findViewById<TextView>(R.id.vendor_name).text.toString()).isEqualTo(vendorName2)
        assertThat(actual.findViewById<TextView>(R.id.vendor_macs).text.toString()).isEqualTo(expected)
        verify(vendorService).findMacAddresses(vendorName2)
        verify(vendorService, never()).findVendorName(vendorName1)
        verify(vendorService, never()).findVendorName(vendorName3)
    }

    @Test
    fun update() {
        // setup
        fixture = spy(VendorAdapter(mainActivity, vendorService))
        whenever(vendorService.findVendors(vendorName2)).thenReturn(vendors)
        doNothing().whenever(fixture).clear()
        doNothing().whenever(fixture).addAll(vendors)
        // execute
        fixture.update(vendorName2)
        // validate
        verify(vendorService).findVendors(vendorName2)
        verify(fixture).clear()
        verify(fixture).addAll(vendors)
    }

    @Test
    fun getViewWhenRootViewNotNull() {
        // setup
        val rootView: View = mock()
        val vendorNameView: TextView = mock()
        val vendorMacsView: TextView = mock()
        val viewGroup = mainActivity.findViewById<ViewGroup>(android.R.id.content)
        val expected = macs.joinToString(separator = ", ")
        whenever(vendorService.findMacAddresses(vendorName2)).thenReturn(macs)
        whenever(rootView.findViewById<TextView>(R.id.vendor_name)).thenReturn(vendorNameView)
        whenever(rootView.findViewById<TextView>(R.id.vendor_macs)).thenReturn(vendorMacsView)
        // execute
        val actual = fixture.getView(1, rootView, viewGroup)
        // validate
        assertThat(actual).isNotNull()
        verify(vendorNameView).text = vendorName2
        verify(vendorMacsView).text = expected
        verify(rootView).findViewById<TextView>(R.id.vendor_name)
        verify(rootView).findViewById<TextView>(R.id.vendor_macs)
        verify(vendorService).findMacAddresses(vendorName2)
    }

}