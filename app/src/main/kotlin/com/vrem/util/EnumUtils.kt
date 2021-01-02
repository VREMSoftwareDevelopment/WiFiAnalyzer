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
package com.vrem.util

fun <T : Enum<T>> findSet(values: Array<T>, indexes: Set<String>, defaultValue: T): Set<T> {
    val results: Set<T> = indexes.map { findOne(values, it.toInt(), defaultValue) }.toSet()
    return if (results.isEmpty()) values.toSet() else results
}

fun <T : Enum<T>> findOne(values: Array<T>, index: Int, defaultValue: T): T =
        if (index in values.indices) values[index] else defaultValue

fun <T : Enum<T>> ordinals(values: Array<T>): Set<String> =
        ordinals(values.toSet())

fun <T : Enum<T>> ordinals(values: Set<T>): Set<String> =
        values.map { it.ordinal.toString() }.toSet()
