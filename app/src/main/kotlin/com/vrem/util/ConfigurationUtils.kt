/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

fun createContext(context: Context, newLocale: Locale): Context =
        if (isMinVersionN()) createContextAndroidN(context, newLocale) else createContextLegacy(context, newLocale)

@TargetApi(Build.VERSION_CODES.N)
private fun createContextAndroidN(context: Context, newLocale: Locale): Context {
    val resources: Resources = context.resources
    val configuration: Configuration = resources.configuration
    configuration.setLocale(newLocale)
    return context.createConfigurationContext(configuration)
}

@Suppress("DEPRECATION")
private fun createContextLegacy(context: Context, newLocale: Locale): Context {
    val resources: Resources = context.resources
    val configuration: Configuration = resources.configuration
    configuration.locale = newLocale
    resources.updateConfiguration(configuration, resources.displayMetrics)
    return context
}
