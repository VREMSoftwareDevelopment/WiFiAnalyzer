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

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.vrem.annotation.OpenClass
import com.vrem.util.ssid
import com.vrem.wifianalyzer.MainContext

/**
 * Holds a [ScanResult] alongside the computed signal level average
 * for that access point across cached scans.
 */

internal class CacheResult(
    val scanResult: ScanResult,
    val average: Int,
)

internal data class CacheKey(
    val bssid: String,
    val ssid: String,
)

@OpenClass
internal class Cache {
    private val scanResults: ArrayDeque<List<ScanResult>> = ArrayDeque(MAXIMUM)
    private var count: Int = COUNT_MIN
    var wifiInfo: WifiInfo? = null
    
    /**
    * Returns one [CacheResult] per unique access point (keyed by BSSID + SSID),
    * with the signal level averaged across all scans currently in the cache.
    */    

    fun scanResults(): List<CacheResult> =
        combineCache()
            .groupingBy { CacheKey(it.BSSID, it.ssid()) }
            .aggregate { _, accumulator: CacheResult?, element, first ->
                CacheResult(element, calculate(first, element, accumulator))
            }.values
            .toList()

    /**
    * Adds a new scan batch to the front of the cache (most recent first).
    * Evicts the oldest batch when the cache exceeds [size].
    * Also advances the internal [count] cycle used by [calculate] when
    * screen size is unavailable.
    */

    fun add(scanResults: List<ScanResult>) {
        count = if (count >= MAXIMUM * FACTOR) COUNT_MIN else count + 1
        val size = size()
        while (this.scanResults.size >= size) {
            this.scanResults.removeLastOrNull()
        }
        this.scanResults.addFirst(scanResults)
    }

    fun first(): List<ScanResult> = scanResults.first()

    fun last(): List<ScanResult> = scanResults.last()

    /**
    * Returns the number of scan batches to retain in the cache.
    * When screen size is available, the cache depth is determined by scan speed:
    * - scanSpeed < 2  → [MAXIMUM] (4 batches)
    * - scanSpeed < 5  → [MAXIMUM] - 1 (3 batches)
    * - scanSpeed < 10 → [MAXIMUM] - 2 (2 batches)
    * - else           → [MINIMUM] (1 batch, effectively no caching)
    * When cache is disabled or screen size is unavailable, returns [MINIMUM].
    */

    fun size(): Int =
        if (sizeAvailable) {
            val settings = MainContext.INSTANCE.settings
            if (settings.cacheOff()) {
                MINIMUM
            } else {
                with(settings.scanSpeed()) {
                    when {
                        this < 2 -> MAXIMUM
                        this < 5 -> MAXIMUM - 1
                        this < 10 -> MAXIMUM - 2
                        else -> MINIMUM
                    }
                }
            }
        } else {
            MINIMUM
        }

    /**
    * Computes the signal level average for an access point across cached scans
    * using an exponential moving average: `(previous + current) / 2`.
    * This weights recent scans more heavily than older ones.
    *
    * When screen size is unavailable (e.g. small screen or restricted mode),
    * applies an additional penalty based on the internal scan [count] cycle
    * to further reduce perceived signal strength, clamped to [-100, 0] dBm.
    */

    private fun calculate(
        first: Boolean,
        element: ScanResult,
        accumulator: CacheResult?,
    ): Int {
        val average: Int = if (first) element.level else (accumulator!!.average + element.level) / DENOMINATOR
        return (if (sizeAvailable) average else average - SIZE * (count + count % FACTOR) / DENOMINATOR)
            .coerceIn(LEVEL_MINIMUM, LEVEL_MAXIMUM)
    }

    private fun combineCache(): List<ScanResult> = scanResults.flatten().sortedWith(comparator())

    private fun comparator(): Comparator<ScanResult> =
        compareBy<ScanResult> {
            it.BSSID
        }.thenBy { it.ssid() }.thenBy { it.level }

    private val sizeAvailable: Boolean get() = MainContext.INSTANCE.configuration.sizeAvailable

    companion object {
        private const val MINIMUM: Int = 1
        private const val MAXIMUM: Int = 4
        private const val SIZE: Int = MINIMUM + MAXIMUM
        private const val LEVEL_MINIMUM: Int = -100
        private const val LEVEL_MAXIMUM: Int = 0
        private const val FACTOR: Int = 3
        private const val DENOMINATOR: Int = 2
        private const val COUNT_MIN: Int = 2
    }
}
