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

import android.annotation.TargetApi
import android.os.Build
import android.text.Html
import android.text.Spanned

val String.Companion.EMPTY: String get() = ""
val String.Companion.SPACE_SEPARATOR: String get() = " "

fun String.specialTrim(): String = this.trim { it <= ' ' }.replace(" +".toRegex(), String.SPACE_SEPARATOR)

fun String.toHtml(color: Int, small: Boolean): String =
        "<font color='" + color + "'><" + (if (small) "small" else "strong") +
                ">" + this + "</" + (if (small) "small" else "strong") + "></font>"

fun String.fromHtml(): Spanned =
        if (buildMinVersionN()) fromHtmlAndroidN() else fromHtmlLegacy()

@TargetApi(Build.VERSION_CODES.N)
private fun String.fromHtmlAndroidN(): Spanned = Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)

@Suppress("DEPRECATION")
private fun String.fromHtmlLegacy(): Spanned = Html.fromHtml(this)

