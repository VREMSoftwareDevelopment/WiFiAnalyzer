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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.*
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.model.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class WarningViewTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val permissionService = MainContextHelper.INSTANCE.permissionService
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper
    private val fixture = spy(WarningView(mainActivity))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(permissionService)
        verifyNoMoreInteractions(wiFiManagerWrapper)
        MainContextHelper.INSTANCE.restore()
        mainActivity.currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun testWarningGone() {
        // setup
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(false).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(false).whenever(fixture).noLocation(registered, permissionService)
        doNothing().whenever(fixture).throttling(registered, wiFiManagerWrapper)
        // execute
        val actual = fixture.update(wiFiData)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.warning).isGone)
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered, permissionService)
        verify(fixture).throttling(registered, wiFiManagerWrapper)
    }

    @Test
    fun testWarningVisibleWhenNoData() {
        // setup
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(true).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(false).whenever(fixture).noLocation(registered, permissionService)
        doNothing().whenever(fixture).throttling(registered, wiFiManagerWrapper)
        // execute
        val actual = fixture.update(wiFiData)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.warning).isVisible)
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered, permissionService)
        verify(fixture).throttling(registered, wiFiManagerWrapper)
    }

    @Test
    fun testWarningVisibleWhenNoLocation() {
        // setup
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(false).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(true).whenever(fixture).noLocation(registered, permissionService)
        doNothing().whenever(fixture).throttling(registered, wiFiManagerWrapper)
        // execute
        val actual = fixture.update(wiFiData)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.warning).isVisible)
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered, permissionService)
        verify(fixture).throttling(registered, wiFiManagerWrapper)
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
        val actual = fixture.noLocation(true, permissionService)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isVisible)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isVisible)
        verify(permissionService).enabled()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    fun testNoLocationVisibleAndThrottlingIsGoneAndroidP() {
        // setup
        whenever(permissionService.enabled()).thenReturn(false)
        // execute
        val actual = fixture.noLocation(true, permissionService)
        // validate
        assertTrue(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isVisible)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
        verify(permissionService).enabled()
    }

    @Test
    fun testNoLocationGoneWhenNotRegistered() {
        // setup
        whenever(permissionService.enabled()).thenReturn(false)
        // execute
        val actual = fixture.noLocation(false, permissionService)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isGone)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
        verify(permissionService, never()).enabled()
    }

    @Test
    fun testNoLocationGoneWithNoPermission() {
        // setup
        whenever(permissionService.enabled()).thenReturn(true)
        // execute
        val actual = fixture.noLocation(true, permissionService)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isGone)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
        verify(permissionService).enabled()
    }

    @Test
    fun testNoLocationGoneWhenNotRegisteredAndNoPermission() {
        // setup
        whenever(permissionService.enabled()).thenReturn(true)
        // execute
        val actual = fixture.noLocation(false, permissionService)
        // validate
        assertFalse(actual)
        assertTrue(mainActivity.findViewById<View>(R.id.no_location).isGone)
        assertTrue(mainActivity.findViewById<View>(R.id.throttling).isGone)
        verify(permissionService, never()).enabled()
    }

    @Test
    fun testThrottlingIsVisibleWhenRegisteredAndThrottlingIsEnabled() {
        // setup
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(true)
        // execute
        fixture.throttling(true, wiFiManagerWrapper)
        // validate
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_throttling)
        assertEquals(View.VISIBLE, textView.visibility)
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
    }

    @Test
    fun testThrottlingIsGoneWhenNotRegisteredAndThrottlingIsEnabled() {
        // setup
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(true)
        // execute
        fixture.throttling(false, wiFiManagerWrapper)
        // validate
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_throttling)
        assertEquals(View.GONE, textView.visibility)
        verify(wiFiManagerWrapper, never()).isScanThrottleEnabled()
    }

    @Test
    fun testThrottlingIsGoneWhenRegisteredAndThrottlingIsDisabled() {
        // setup
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(false)
        // execute
        fixture.throttling(true, wiFiManagerWrapper)
        // validate
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_throttling)
        assertEquals(View.GONE, textView.visibility)
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
    }

}