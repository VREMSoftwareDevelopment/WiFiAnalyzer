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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.util.EMPTY
import java.net.InetAddress
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

private const val DISTANCE_MHZ_M = 27.55
private const val MIN_RSSI = -100
private const val MAX_RSSI = -55
private const val QUOTE = "\""

fun calculateDistance(frequency: Int, level: Int): Double =
    10.0.pow((DISTANCE_MHZ_M - 20 * log10(frequency.toDouble()) + abs(level)) / 20.0)

fun calculateSignalLevel(rssi: Int, numLevels: Int): Int = when {
    rssi <= MIN_RSSI -> 0
    rssi >= MAX_RSSI -> numLevels - 1
    else -> (rssi - MIN_RSSI) * (numLevels - 1) / (MAX_RSSI - MIN_RSSI)
}

fun convertSSID(ssid: String): String = ssid.removePrefix(QUOTE).removeSuffix(QUOTE)

fun convertIpV4Address(ipV4Address: Int): String {
    return try {
        val value: Long = when (ByteOrder.LITTLE_ENDIAN) {
            ByteOrder.nativeOrder() -> Integer.reverseBytes(ipV4Address).toLong()
            else -> ipV4Address.toLong()
        }
        return InetAddress.getByAddress(value.toBigInteger().toByteArray()).hostAddress ?: String.EMPTY
    } catch (e: Exception) {
        String.EMPTY
    }
}
