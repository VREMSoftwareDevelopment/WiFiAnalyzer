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
package com.vrem.wifianalyzer.wifi.scanner

import android.os.Handler
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.settings.Settings

@OpenClass
internal class PeriodicScan(private val scanner: ScannerService, private val handler: Handler, private val settings: Settings) : Runnable {
    internal var running = false

    fun stop() {
        handler.removeCallbacks(this)
        running = false
    }

    fun start() {
        nextRun(DELAY_INITIAL)
    }

    fun startWithDelay() {
        nextRun(settings.scanSpeed() * DELAY_INTERVAL)
    }

    override fun run() {
        scanner.update()
        startWithDelay()
    }

    private fun nextRun(delay: Long) {
        stop()
        handler.postDelayed(this, delay)
        running = true
    }

    companion object {
        private const val DELAY_INITIAL = 1L
        const val DELAY_INTERVAL = 1000L
    }
}