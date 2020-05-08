/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.os.Build

fun isMinVersionQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun isMinVersionP(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

fun isVersionP(): Boolean = Build.VERSION.SDK_INT == Build.VERSION_CODES.P

fun isMinVersionN(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

fun isMinVersionM(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun isMinVersionL(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
