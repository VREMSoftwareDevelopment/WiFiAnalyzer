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
package com.vrem.wifianalyzer.wifi.scanner

import com.vrem.wifianalyzer.Configuration
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.permission.PermissionService
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import kotlinx.coroutines.CoroutineScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoMoreInteractions

class ScannerServiceTest {
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val mainActivity: MainActivity = mock()
    private val permissionService: PermissionService = mock()
    private val coroutineScope: CoroutineScope = mock()
    private val settings: Settings = mock()
    private val configuration: Configuration = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wiFiManagerWrapper)
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(permissionService)
        verifyNoMoreInteractions(coroutineScope)
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(configuration)
    }

    @Test
    fun makeScannerService() {
        // Act
        val actual =
            makeScannerService(
                mainActivity,
                wiFiManagerWrapper,
                permissionService,
                coroutineScope,
                settings,
                configuration,
            ) as Scanner
        // Assert
        assertThat(actual.wiFiManagerWrapper).isEqualTo(wiFiManagerWrapper)
        assertThat(actual.settings).isEqualTo(settings)
        assertThat(actual.permissionService).isEqualTo(permissionService)
        assertThat(actual.transformer).isNotNull()
        assertThat(actual.periodicScan).isNotNull()
        assertThat(actual.scannerCallback).isNotNull()
        assertThat(actual.scanResultsReceiver).isNotNull()
        assertThat(actual.running()).isFalse
    }
}
