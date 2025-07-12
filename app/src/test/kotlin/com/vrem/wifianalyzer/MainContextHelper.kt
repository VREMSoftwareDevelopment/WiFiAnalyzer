/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer

import com.vrem.wifianalyzer.permission.PermissionService
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.vendor.model.VendorService
import com.vrem.wifianalyzer.wifi.filter.adapter.FiltersAdapter
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import com.vrem.wifianalyzer.wifi.scanner.ScannerService
import org.mockito.kotlin.mock

enum class MainContextHelper {
    INSTANCE;

    private val saved: MutableMap<Class<*>, Any> = mutableMapOf()
    private val mainContext: MainContext = MainContext.INSTANCE

    val settings: Settings
        get() {
            runCatching { saved[Settings::class.java] = mainContext.settings }
            mainContext.settings = mock()
            return mainContext.settings
        }

    val vendorService: VendorService
        get() {
            runCatching { saved[VendorService::class.java] = mainContext.vendorService }
            mainContext.vendorService = mock()
            return mainContext.vendorService
        }

    val permissionService: PermissionService
        get() {
            runCatching { saved[PermissionService::class.java] = mainContext.permissionService }
            mainContext.permissionService = mock()
            return mainContext.permissionService
        }

    val scannerService: ScannerService
        get() {
            runCatching { saved[ScannerService::class.java] = mainContext.scannerService }
            mainContext.scannerService = mock()
            return mainContext.scannerService
        }

    val mainActivity: MainActivity
        get() {
            runCatching { saved[MainActivity::class.java] = mainContext.mainActivity }
            mainContext.mainActivity = mock()
            return mainContext.mainActivity
        }

    val configuration: Configuration
        get() {
            runCatching { saved[Configuration::class.java] = mainContext.configuration }
            mainContext.configuration = mock()
            return mainContext.configuration
        }

    val filterAdapter: FiltersAdapter
        get() {
            runCatching { saved[FiltersAdapter::class.java] = mainContext.filtersAdapter }
            mainContext.filtersAdapter = mock()
            return mainContext.filtersAdapter
        }

    val wiFiManagerWrapper: WiFiManagerWrapper
        get() {
            runCatching { saved[WiFiManagerWrapper::class.java] = mainContext.wiFiManagerWrapper }
            mainContext.wiFiManagerWrapper = mock()
            return mainContext.wiFiManagerWrapper
        }

    fun restore() {
        saved.entries.forEach {
            when (it.key) {
                Settings::class.java -> mainContext.settings = it.value as Settings
                VendorService::class.java -> mainContext.vendorService = it.value as VendorService
                ScannerService::class.java -> mainContext.scannerService = it.value as ScannerService
                MainActivity::class.java -> mainContext.mainActivity = it.value as MainActivity
                Configuration::class.java -> mainContext.configuration = it.value as Configuration
                FiltersAdapter::class.java -> mainContext.filtersAdapter = it.value as FiltersAdapter
                WiFiManagerWrapper::class.java -> mainContext.wiFiManagerWrapper = it.value as WiFiManagerWrapper
            }
        }
        saved.clear()
    }

}
