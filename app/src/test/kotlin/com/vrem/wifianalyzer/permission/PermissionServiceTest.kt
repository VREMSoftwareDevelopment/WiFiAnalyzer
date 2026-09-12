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

import com.vrem.wifianalyzer.MainActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class PermissionServiceTest {
    private val mainActivity: MainActivity = mock()
    private val locationPermission: LocationPermission = mock()
    private val applicationPermission: ApplicationPermission = mock()
    private val fixture = PermissionService(mainActivity, locationPermission, applicationPermission)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(applicationPermission)
        verifyNoMoreInteractions(locationPermission)
    }

    @Test
    fun enabled() {
        // Arrange
        whenever(locationPermission.enabled()).thenReturn(true)
        whenever(applicationPermission.granted()).thenReturn(true)
        // Act
        val actual = fixture.enabled()
        // Assert
        assertThat(actual).isTrue
        verify(locationPermission).enabled()
        verify(applicationPermission).granted()
    }

    @Test
    fun enabledWhenLocationPermissionIsNotEnabled() {
        // Arrange
        whenever(locationPermission.enabled()).thenReturn(false)
        // Act
        val actual = fixture.enabled()
        // Assert
        assertThat(actual).isFalse
        verify(locationPermission).enabled()
    }

    @Test
    fun enabledWhenApplicationPermissionAreNotGranted() {
        // Arrange
        whenever(locationPermission.enabled()).thenReturn(true)
        whenever(applicationPermission.granted()).thenReturn(false)
        // Act
        val actual = fixture.enabled()
        // Assert
        assertThat(actual).isFalse
        verify(locationPermission).enabled()
        verify(applicationPermission).granted()
    }

    @Test
    fun systemEnabled() {
        // Arrange
        whenever(locationPermission.enabled()).thenReturn(true)
        // Act
        val actual = fixture.locationEnabled()
        // Assert
        assertThat(actual).isTrue
        verify(locationPermission).enabled()
    }

    @Test
    fun permissionGranted() {
        // Arrange
        whenever(applicationPermission.granted()).thenReturn(true)
        // Act
        val actual = fixture.permissionGranted()
        // Assert
        assertThat(actual).isTrue
        verify(applicationPermission).granted()
    }

    @Test
    fun permissionCheck() {
        // Act
        fixture.check()
        // Assert
        verify(applicationPermission).check()
    }
}
