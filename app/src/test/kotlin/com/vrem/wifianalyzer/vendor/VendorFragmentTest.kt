/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.RobolectricUtil
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class VendorFragmentTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val vendorService = INSTANCE.vendorService
    private val fixture = VendorFragment()

    @Before
    fun setUp() {
        whenever(vendorService.findVendors()).thenReturn(emptyList())
        RobolectricUtil.INSTANCE.startFragment(fixture)
    }

    @After
    fun tearDown() {
        verify(vendorService).findVendors()
        INSTANCE.restore()
    }

    @Test
    fun testListenerOnQueryTextChange() {
        // setup
        val values = "     ABS       ADF      "
        val expected = "ABS ADF"
        val vendorAdapter = mock(VendorAdapter::class.java)
        val fixture = VendorFragment.Listener(vendorAdapter)
        // execute
        val actual = fixture.onQueryTextChange(values)
        // verify
        assertTrue(actual)
        verify(vendorAdapter).update(expected)
    }

    @Test
    fun testListenerOnQueryTextChangeWithNull() {
        // setup
        val vendorAdapter = mock(VendorAdapter::class.java)
        val fixture = VendorFragment.Listener(vendorAdapter)
        // execute
        val actual = fixture.onQueryTextChange(String.EMPTY)
        // verify
        assertTrue(actual)
        verify(vendorAdapter).update(String.EMPTY)
    }

    @Test
    fun testListenerOnQueryTextSubmit() {
        // setup
        val vendorAdapter = mock(VendorAdapter::class.java)
        val fixture = VendorFragment.Listener(vendorAdapter)
        // execute
        val actual = fixture.onQueryTextSubmit(String.EMPTY)
        // verify
        assertFalse(actual)
    }
}