/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2022 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionServiceTest {
    private val activity: Activity = mock()
    private val locationPermission: LocationPermission = mock()
    private val applicationPermission: ApplicationPermission = mock()
    private val fixture = PermissionService(activity, locationPermission, applicationPermission)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(activity)
        verifyNoMoreInteractions(applicationPermission)
        verifyNoMoreInteractions(locationPermission)
    }

    @Test
    fun testEnabled() {
        // setup
        whenever(locationPermission.enabled()).thenReturn(true)
        whenever(applicationPermission.granted()).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(locationPermission).enabled()
        verify(applicationPermission).granted()
    }

    @Test
    fun testEnabledWhenLocationPermissionIsNotEnabled() {
        // setup
        whenever(locationPermission.enabled()).thenReturn(false)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(locationPermission).enabled()
    }

    @Test
    fun testEnabledWhenApplicationPermissionAreNotGranted() {
        // setup
        whenever(locationPermission.enabled()).thenReturn(true)
        whenever(applicationPermission.granted()).thenReturn(false)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(locationPermission).enabled()
        verify(applicationPermission).granted()
    }

    @Test
    fun testSystemEnabled() {
        // setup
        whenever(locationPermission.enabled()).thenReturn(true)
        // execute
        val actual = fixture.locationEnabled()
        // validate
        assertTrue(actual)
        verify(locationPermission).enabled()
    }

    @Test
    fun testPermissionGranted() {
        // setup
        whenever(applicationPermission.granted()).thenReturn(true)
        // execute
        val actual = fixture.permissionGranted()
        // validate
        assertTrue(actual)
        verify(applicationPermission).granted()
    }

    @Test
    fun testPermissionCheck() {
        // execute
        fixture.check()
        // validate
        verify(applicationPermission).check()
    }

    @Test
    fun testGranted() {
        // setup
        val requestCode = 111
        val results = intArrayOf(1, 2, 3)
        whenever(applicationPermission.granted(requestCode, results)).thenReturn(true)
        // execute
        val actual = fixture.granted(requestCode, results)
        // validate
        assertTrue(actual)
        verify(applicationPermission).granted(requestCode, results)
    }
}