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
import java.util.zip.ZipInputStream

fun readFile(
    resources: Resources,
    @RawRes id: Int,
): String =
    runCatching {
        resources
            .openRawResource(id)
            .bufferedReader()
            .use { it.readText() }
            .replace("\r", String.EMPTY)
    }.getOrDefault(String.EMPTY)

fun readZipFile(
    resources: Resources,
    @RawRes id: Int,
): List<String> =
    runCatching {
        resources.openRawResource(id).use { inputStream ->
            ZipInputStream(inputStream).use { zipInputStream ->
                zipInputStream.nextEntry
                zipInputStream.bufferedReader().readLines()
            }
        }
    }.getOrDefault(emptyList())
