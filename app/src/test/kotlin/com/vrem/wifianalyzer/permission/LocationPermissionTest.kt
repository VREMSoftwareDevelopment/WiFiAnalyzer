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
package com.vrem.wifianalyzer.permission

import android.app.Activity
import android.location.LocationManager
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class LocationPermissionTest {
    private val activity: Activity = mock()
    private val locationManager: LocationManager = mock()
    private val fixture: LocationPermission = LocationPermission(activity)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(activity)
        verifyNoMoreInteractions(locationManager)
    }

    @Test
    fun testEnabledWhenGPSProviderIsEnabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun testEnabledWhenLocationEnabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
    }

    @Test
    fun testEnabledWhenNetworkProviderEnabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @Test
    fun testEnabledWhenAllProvidersAreDisabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun testEnabledWhenAllProvidersThrowException() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenThrow(RuntimeException::class.java)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenThrow(RuntimeException::class.java)
        whenever(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenThrow(RuntimeException::class.java)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun testEnabledWhenException() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenThrow(RuntimeException::class.java)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(activity).getSystemService(LocationManager::class.java)
    }

    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    @Test
    fun testEnabledReturnsTrueLegacy() {
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity, never()).getSystemService(any())
    }

}