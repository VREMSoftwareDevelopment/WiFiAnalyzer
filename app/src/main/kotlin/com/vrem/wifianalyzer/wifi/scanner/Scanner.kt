/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import com.vrem.wifianalyzer.wifi.model.WiFiData

internal class Scanner(private val wiFiManagerWrapper: WiFiManagerWrapper,
                       private val settings: Settings,
                       private val cache: Cache = Cache(),
                       private val transformer: Transformer = Transformer()) : ScannerService {
    private val updateNotifiers: MutableList<UpdateNotifier> = mutableListOf()
    private var wiFiData: WiFiData = WiFiData.EMPTY
    lateinit var periodicScan: PeriodicScan

    override fun update() {
        wiFiManagerWrapper.enableWiFi()
        scanResults()
        wiFiData = transformer.transformToWiFiData(cache.scanResults(), cache.wifiInfo())
        updateNotifiers.forEach { it.update(wiFiData) }
    }

    override fun wiFiData(): WiFiData = wiFiData

    override fun register(updateNotifier: UpdateNotifier): Boolean = updateNotifiers.add(updateNotifier)

    override fun unregister(updateNotifier: UpdateNotifier): Boolean = updateNotifiers.remove(updateNotifier)

    override fun pause(): Unit = periodicScan.stop()

    override fun running(): Boolean = periodicScan.running

    override fun resume(): Unit = periodicScan.start()

    override fun stop() {
        if (settings.wiFiOffOnExit()) {
            wiFiManagerWrapper.disableWiFi()
        }
    }

    override fun toggle(): Unit =
            if (periodicScan.running) {
                periodicScan.stop()
            } else {
                periodicScan.start()
            }

    fun registered(): Int = updateNotifiers.size

    private fun scanResults() {
        try {
            if (wiFiManagerWrapper.startScan()) {
                val scanResults: List<ScanResult> = wiFiManagerWrapper.scanResults()
                val wifiInfo: WifiInfo? = wiFiManagerWrapper.wiFiInfo()
                cache.add(scanResults, wifiInfo)
            }
        } catch (e: Exception) {
            // critical error: do not die
        }
    }

}