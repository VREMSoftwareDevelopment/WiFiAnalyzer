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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.os.Build
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.*
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.model.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class WarningViewTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper
    private val permissionService = MainContextHelper.INSTANCE.permissionService
    private val fixture = spy(WarningView(mainActivity))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wiFiManagerWrapper)
        verifyNoMoreInteractions(permissionService)
        MainContextHelper.INSTANCE.restore()
        mainActivity.currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun testWarningGone() {
        // setup
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(false).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(false).whenever(fixture).noLocation(registered)
        doReturn(false).whenever(fixture).throttling(registered, noData = false, noLocation = false)
        // execute
        val actual = fixture.update(wiFiData)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.warning).isGone)
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered)
        verify(fixture).throttling(registered, noData = false, noLocation = false)
    }

    @Test
    fun testWarningVisibleWhenNoData() {
        // setup
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(true).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(false).whenever(fixture).noLocation(registered)
        doReturn(false).whenever(fixture).throttling(registered, noData = true, noLocation = false)
        // execute
        val actual = fixture.update(wiFiData)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.warning).isVisible)
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered)
        verify(fixture).throttling(registered, noData = true, noLocation = false)
    }

    @Test
    fun testWarningVisibleWhenNoLocation() {
        // setup
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(false).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(true).whenever(fixture).noLocation(registered)
        doReturn(false).whenever(fixture).throttling(registered, noData = false, noLocation = true)
        // execute
        val actual = fixture.update(wiFiData)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.warning).isVisible)
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered)
        verify(fixture).throttling(registered, noData = false, noLocation = true)
    }

    @Test
    fun testWarningVisibleWhenThrottling() {
        // setup
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(false).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(false).whenever(fixture).noLocation(registered)
        doReturn(true).whenever(fixture).throttling(registered, noData = false, noLocation = false)
        // execute
        val actual = fixture.update(wiFiData)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.warning).isVisible)
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered)
        verify(fixture).throttling(registered, noData = false, noLocation = false)
    }

    @Test
    fun testNoDataVisible() {
        // setup
        val wiFiDetails: List<WiFiDetail> = listOf()
        // execute
        val actual = fixture.noData(true, wiFiDetails)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_data).isVisible)
    }

    @Test
    fun testNoDataGoneWhenNotRegistered() {
        // setup
        val wiFiDetails: List<WiFiDetail> = listOf()
        // execute
        fixture.noData(false, wiFiDetails)
        // validate
        assertTrue(mainActivity.findViewById<View>(R.id.no_data).isGone)
    }

    @Test
    fun testNoDataGoneWithWiFiDetails() {
        // setup
        val wiFiDetails: List<WiFiDetail> = listOf(WiFiDetail.EMPTY)
        // execute
        val actual = fixture.noData(true, wiFiDetails)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_data).isGone)
    }

    @Test
    fun testNoDataGoneWhenNotRegisteredAndWithWiFiDetails() {
        // setup
        val wiFiDetails: List<WiFiDetail> = listOf(WiFiDetail.EMPTY)
        // execute
        val actual = fixture.noData(false, wiFiDetails)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_data).isGone)
    }

    @Test
    fun testNoLocationVisible() {
        // setup
        whenever(permissionService.enabled()).thenReturn(false)
        // execute
        val actual = fixture.noLocation(true)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isVisible)
        verify(permissionService).enabled()
    }

    @Test
    fun testNoLocationGoneWhenNotRegistered() {
        // setup
        whenever(permissionService.enabled()).thenReturn(false)
        // execute
        val actual = fixture.noLocation(false)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isGone)
        verify(permissionService, never()).enabled()
    }

    @Test
    fun testNoLocationGoneWithNoPermission() {
        // setup
        whenever(permissionService.enabled()).thenReturn(true)
        // execute
        val actual = fixture.noLocation(true)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isGone)
        verify(permissionService).enabled()
    }

    @Test
    fun testNoLocationGoneWhenNotRegisteredAndNoPermission() {
        // setup
        whenever(permissionService.enabled()).thenReturn(true)
        // execute
        val actual = fixture.noLocation(false)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isGone)
        verify(permissionService, never()).enabled()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU])
    fun testThrottlingAndroidTVisibleWhenRegisteredAndThrottling() {
        // setup
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(true)
        // execute
        val actual = fixture.throttling(registered = true, noData = true, noLocation = true)
        //
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isVisible)
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU])
    fun testThrottlingAndroidTGoneWhenNotRegistered() {
        // setup
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(true)
        // execute
        val actual = fixture.throttling(registered = false, noData = true, noLocation = true)
        //
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
        verify(wiFiManagerWrapper, never()).isScanThrottleEnabled()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU])
    fun testThrottlingAndroidTGoneWhenNoThrottling() {
        // setup
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(false)
        // execute
        val actual = fixture.throttling(registered = true, noData = true, noLocation = true)
        //
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun testThrottlingAndroidPGoneWhenNotRegistered() {
        // execute
        val actual = fixture.throttling(registered = false, noData = true, noLocation = true)
        //
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun testThrottlingAndroidPGoneWhenRegisteredAndNoDataAndNoLocation() {
        // execute
        val actual = fixture.throttling(registered = true, noData = false, noLocation = false)
        //
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun testThrottlingAndroidPVisibleWhenRegisteredAndNoData() {
        // execute
        val actual = fixture.throttling(registered = true, noData = true, noLocation = false)
        //
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isVisible)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun testThrottlingAndroidPVisibleWhenRegisteredAndNoLocation() {
        // execute
        val actual = fixture.throttling(registered = true, noData = false, noLocation = true)
        //
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isVisible)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    fun testThrottlingLegacyGone() {
        // execute
        val actual = fixture.throttling(registered = true, noData = true, noLocation = true)
        //
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
    }

}