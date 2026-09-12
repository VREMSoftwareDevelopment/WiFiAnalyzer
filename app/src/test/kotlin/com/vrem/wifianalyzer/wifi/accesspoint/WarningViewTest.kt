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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.permission.PermissionService
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class WarningViewTest {
    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val permissionService: PermissionService = mock()
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val fixture = spy(WarningView(mainActivity, wiFiManagerWrapper, permissionService))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(permissionService)
        verifyNoMoreInteractions(wiFiManagerWrapper)
        mainActivity.currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun warningGone() {
        // Arrange
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(false).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(false).whenever(fixture).noLocation(registered)
        doNothing().whenever(fixture).throttling(registered)
        // Act
        val actual = fixture.update(wiFiData)
        // Assert
        assertThat(actual).isFalse
        assertThat(mainActivity.findViewById<View>(R.id.warning).isGone).isTrue
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered)
        verify(fixture).throttling(registered)
    }

    @Test
    fun warningVisibleWhenNoData() {
        // Arrange
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(true).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(false).whenever(fixture).noLocation(registered)
        doNothing().whenever(fixture).throttling(registered)
        // Act
        val actual = fixture.update(wiFiData)
        // Assert
        assertThat(actual).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.warning).isVisible).isTrue
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered)
        verify(fixture).throttling(registered)
    }

    @Test
    fun warningVisibleWhenNoLocation() {
        // Arrange
        val wiFiData = WiFiData.EMPTY
        val registered = mainActivity.currentNavigationMenu().registered()
        doReturn(false).whenever(fixture).noData(registered, wiFiData.wiFiDetails)
        doReturn(true).whenever(fixture).noLocation(registered)
        doNothing().whenever(fixture).throttling(registered)
        // Act
        val actual = fixture.update(wiFiData)
        // Assert
        assertThat(actual).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.warning).isVisible).isTrue
        verify(fixture).noData(registered, wiFiData.wiFiDetails)
        verify(fixture).noLocation(registered)
        verify(fixture).throttling(registered)
    }

    @Test
    fun noDataVisible() {
        // Arrange
        val wiFiDetails: List<WiFiDetail> = listOf()
        // Act
        val actual = fixture.noData(true, wiFiDetails)
        // Assert
        assertThat(actual).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.no_data).isVisible).isTrue
    }

    @Test
    fun noDataGoneWhenNotRegistered() {
        // Arrange
        val wiFiDetails: List<WiFiDetail> = listOf()
        // Act
        fixture.noData(false, wiFiDetails)
        // Assert
        assertThat(mainActivity.findViewById<View>(R.id.no_data).isGone).isTrue
    }

    @Test
    fun noDataGoneWithWiFiDetails() {
        // Arrange
        val wiFiDetails: List<WiFiDetail> = listOf(WiFiDetail.EMPTY)
        // Act
        val actual = fixture.noData(true, wiFiDetails)
        // Assert
        assertThat(actual).isFalse
        assertThat(mainActivity.findViewById<View>(R.id.no_data).isGone).isTrue
    }

    @Test
    fun noDataGoneWhenNotRegisteredAndWithWiFiDetails() {
        // Arrange
        val wiFiDetails: List<WiFiDetail> = listOf(WiFiDetail.EMPTY)
        // Act
        val actual = fixture.noData(false, wiFiDetails)
        // Assert
        assertThat(actual).isFalse
        assertThat(mainActivity.findViewById<View>(R.id.no_data).isGone).isTrue
    }

    @Test
    fun noLocationVisible() {
        // Arrange
        whenever(permissionService.enabled()).thenReturn(false)
        // Act
        val actual = fixture.noLocation(true)
        // Assert
        assertThat(actual).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.no_location).isVisible).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.throttling).isVisible).isTrue
        verify(permissionService).enabled()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    fun noLocationVisibleAndThrottlingIsGoneAndroidP() {
        // Arrange
        whenever(permissionService.enabled()).thenReturn(false)
        // Act
        val actual = fixture.noLocation(true)
        // Assert
        assertThat(actual).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.no_location).isVisible).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.throttling).isGone).isTrue
        verify(permissionService).enabled()
    }

    @Test
    fun noLocationGoneWhenNotRegistered() {
        // Arrange
        whenever(permissionService.enabled()).thenReturn(false)
        // Act
        val actual = fixture.noLocation(false)
        // Assert
        assertThat(actual).isFalse
        assertThat(mainActivity.findViewById<View>(R.id.no_location).isGone).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.throttling).isGone).isTrue
        verify(permissionService, never()).enabled()
    }

    @Test
    fun noLocationGoneWithNoPermission() {
        // Arrange
        whenever(permissionService.enabled()).thenReturn(true)
        // Act
        val actual = fixture.noLocation(true)
        // Assert
        assertThat(actual).isFalse
        assertThat(mainActivity.findViewById<View>(R.id.no_location).isGone).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.throttling).isGone).isTrue
        verify(permissionService).enabled()
    }

    @Test
    fun noLocationGoneWhenNotRegisteredAndNoPermission() {
        // Arrange
        whenever(permissionService.enabled()).thenReturn(true)
        // Act
        val actual = fixture.noLocation(false)
        // Assert
        assertThat(actual).isFalse
        assertThat(mainActivity.findViewById<View>(R.id.no_location).isGone).isTrue
        assertThat(mainActivity.findViewById<View>(R.id.throttling).isGone).isTrue
        verify(permissionService, never()).enabled()
    }

    @Test
    fun throttlingIsVisibleWhenRegisteredAndThrottlingIsEnabled() {
        // Arrange
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(true)
        // Act
        fixture.throttling(true)
        // Assert
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_throttling)
        assertThat(textView.visibility).isEqualTo(View.VISIBLE)
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
    }

    @Test
    fun throttlingIsGoneWhenNotRegisteredAndThrottlingIsEnabled() {
        // Arrange
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(true)
        // Act
        fixture.throttling(false)
        // Assert
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_throttling)
        assertThat(textView.visibility).isEqualTo(View.GONE)
        verify(wiFiManagerWrapper, never()).isScanThrottleEnabled()
    }

    @Test
    fun throttlingIsGoneWhenRegisteredAndThrottlingIsDisabled() {
        // Arrange
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(false)
        // Act
        fixture.throttling(true)
        // Assert
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_throttling)
        assertThat(textView.visibility).isEqualTo(View.GONE)
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
    }
}
