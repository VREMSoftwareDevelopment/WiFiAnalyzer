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
package com.vrem.util

import android.content.res.Resources
import androidx.annotation.RawRes
import java.io.InputStream

fun readFile(resources: Resources, @RawRes id: Int): String {
    return try {
        resources.openRawResource(id).use { read(it) }
    } catch (e: Exception) {
        String.EMPTY
    }
}

private fun read(inputStream: InputStream): String {
    val size = inputStream.available()
    val bytes = ByteArray(size)
    val count = inputStream.read(bytes)
    return if (count == size) String(bytes).replace("\r", "") else String.EMPTY
}
