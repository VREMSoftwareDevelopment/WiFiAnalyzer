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
import android.text.TextUtils

private const val SEPARATOR = " "

fun split(source: String?): Set<String> =
        if (source == null || source.isBlank()) HashSet() else trim(source).split(SEPARATOR).toSet()

fun join(source: Set<String?>?): String =
        if (source == null) String.EMPTY else trim(TextUtils.join(SEPARATOR, source.toTypedArray()))

fun trim(source: String?): String =
        if (source == null || source.isBlank()) String.EMPTY else source.trim { it <= ' ' }.replace(" +".toRegex(), " ")

fun textToHtml(text: String, color: Int, small: Boolean): String =
        "<font color='" + color + "'><" + (if (small) "small" else "strong") +
                ">" + text + "</" + (if (small) "small" else "strong") + "></font>"

fun fromHtml(text: String): Spanned =
        if (buildMinVersionN()) fromHtmlAndroidN(text) else fromHtmlLegacy(text)

@TargetApi(Build.VERSION_CODES.N)
private fun fromHtmlAndroidN(text: String): Spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

@Suppress("DEPRECATION")
private fun fromHtmlLegacy(text: String): Spanned = Html.fromHtml(text)
