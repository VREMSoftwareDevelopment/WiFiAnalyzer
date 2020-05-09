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
package com.vrem.wifianalyzer.permission

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SystemPermissionTest {
    private val activity: Activity = mock(Activity::class.java)
    private val locationManager: LocationManager = mock(LocationManager::class.java)
    private val fixture: SystemPermission = SystemPermission(activity)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(activity)
        verifyNoMoreInteractions(locationManager)
    }

    @Test
    fun testEnabledWhenGPSProviderIsEnabled() {
        // setup
        whenever(activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(Context.LOCATION_SERVICE)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun testEnabledWhenLocationIsEnabled() {
        // setup
        whenever(activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(Context.LOCATION_SERVICE)
        verify(locationManager).isLocationEnabled
    }

    @Test
    fun testEnabledWhenNetworkProviderIsEnabled() {
        // setup
        whenever(activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(Context.LOCATION_SERVICE)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @Test
    fun testEnabledWhenAllProvidersAreDisabled() {
        // setup
        whenever(activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(activity).getSystemService(Context.LOCATION_SERVICE)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun testEnabled() {
        // setup
        whenever(activity.getSystemService(Context.LOCATION_SERVICE)).thenThrow(RuntimeException::class.java)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(activity).getSystemService(Context.LOCATION_SERVICE)
    }
}