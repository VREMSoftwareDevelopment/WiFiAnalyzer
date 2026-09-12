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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vrem.wifianalyzer.Configuration
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.permission.PermissionService
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import com.vrem.wifianalyzer.wifi.model.WiFiData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface ScannerService {
    fun update()

    fun wiFiData(): StateFlow<WiFiData>

    fun pause()

    fun running(): Boolean

    fun resume()

    fun stop()

    fun toggle()
}

fun makeScannerService(
    mainActivity: MainActivity,
    wiFiManagerWrapper: WiFiManagerWrapper,
    permissionService: PermissionService,
    coroutineScope: CoroutineScope,
    settings: Settings,
    configuration: Configuration,
): ScannerService {
    val cache = Cache(settings, configuration)
    val transformer = Transformer(cache)
    val scanner = Scanner(wiFiManagerWrapper, settings, permissionService, transformer)
    scanner.periodicScan = PeriodicScan(scanner, coroutineScope, settings)
    scanner.scannerCallback = ScannerCallback(wiFiManagerWrapper, cache)
    scanner.scanResultsReceiver = ScanResultsReceiver(mainActivity, scanner.scannerCallback)
    return scanner
}

fun LifecycleOwner.collectWiFiData(update: (WiFiData) -> Unit) {
    val wiFiData = MainContext.INSTANCE.scannerService.wiFiData()
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            wiFiData.collect(update)
        }
    }
}
