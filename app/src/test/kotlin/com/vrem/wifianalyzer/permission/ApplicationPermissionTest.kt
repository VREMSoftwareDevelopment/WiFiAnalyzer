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
package com.vrem.wifianalyzer.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class ApplicationPermissionTest {
    private val mainActivity: MainActivity = mock()
    private val permissionDialog: PermissionDialog = mock()
    private val fixture = ApplicationPermission(mainActivity, permissionDialog)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
    }

    @Test
    fun checkWithFineLocationGranted() {
        // Arrange
        doReturn(
            PackageManager.PERMISSION_GRANTED,
        ).whenever(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        // Act
        fixture.check()
        // Assert
        verify(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        verify(mainActivity, never()).isFinishing
        verify(permissionDialog, never()).show()
    }

    @Test
    fun checkWithActivityFinish() {
        // Arrange
        doReturn(
            PackageManager.PERMISSION_DENIED,
        ).whenever(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        doReturn(true).whenever(mainActivity).isFinishing
        // Act
        fixture.check()
        // Assert
        verify(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        verify(mainActivity).isFinishing
        verify(permissionDialog, never()).show()
    }

    @Test
    fun checkWithRequestPermissions() {
        // Arrange
        whenever(
            mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION),
        ).thenReturn(PackageManager.PERMISSION_DENIED)
        whenever(mainActivity.isFinishing).thenReturn(false)
        // Act
        fixture.check()
        // Assert
        verify(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        verify(mainActivity).isFinishing
        verify(permissionDialog).show()
    }

    @Test
    fun grantedWhenPermissionGranted() {
        // Arrange
        doReturn(
            PackageManager.PERMISSION_GRANTED,
        ).whenever(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        // Act
        val actual = fixture.granted()
        // Assert
        assertThat(actual).isTrue
        verify(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @Test
    fun grantedWhenPermissionDenied() {
        // Arrange
        doReturn(
            PackageManager.PERMISSION_DENIED,
        ).whenever(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        // Act
        val actual = fixture.granted()
        // Assert
        assertThat(actual).isFalse
        verify(mainActivity).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
