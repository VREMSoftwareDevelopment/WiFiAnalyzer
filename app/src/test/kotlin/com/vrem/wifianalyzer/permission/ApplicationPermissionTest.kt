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

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ApplicationPermissionTest {
    private val activity: Activity = mock()
    private val permissionDialog: PermissionDialog = mock()
    private val fixture = ApplicationPermission(activity, permissionDialog)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(activity)
    }

    @Test
    fun checkWithFineLocationGranted() {
        // setup
        whenever(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(PackageManager.PERMISSION_GRANTED)
        // execute
        fixture.check()
        // validate
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        verify(activity, never()).isFinishing
        verify(activity, never()).requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ApplicationPermission.REQUEST_CODE)
    }

    @Test
    fun checkWithActivityFinish() {
        // setup
        whenever(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(PackageManager.PERMISSION_DENIED)
        whenever(activity.isFinishing).thenReturn(true)
        // execute
        fixture.check()
        // validate
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        verify(activity).isFinishing
        verify(activity, never()).requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ApplicationPermission.REQUEST_CODE)
    }

    @Test
    fun checkWithRequestPermissions() {
        // setup
        whenever(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(PackageManager.PERMISSION_DENIED)
        whenever(activity.isFinishing).thenReturn(false)
        // execute
        fixture.check()
        // validate
        verify(activity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        verify(activity).isFinishing
        verify(permissionDialog).show()
    }

    @Test
    fun granted() {
        // setup
        val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)
        // execute
        val actual = fixture.granted(ApplicationPermission.REQUEST_CODE, grantResults)
        // validate
        assertThat(actual).isTrue()
    }

    @Test
    fun grantedWithOtherRequestCode() {
        // setup
        val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)
        // execute
        val actual = fixture.granted(-ApplicationPermission.REQUEST_CODE, grantResults)
        // validate
        assertThat(actual).isFalse()
    }

    @Test
    fun grantedWithNoResults() {
        // setup
        val grantResults = intArrayOf()
        // execute
        val actual = fixture.granted(ApplicationPermission.REQUEST_CODE, grantResults)
        // validate
        assertThat(actual).isFalse()
    }

    @Test
    fun grantedWithNoPermissionGranted() {
        // setup
        val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)
        // execute
        val actual = fixture.granted(ApplicationPermission.REQUEST_CODE, grantResults)
        // validate
        assertThat(actual).isFalse()
    }
}