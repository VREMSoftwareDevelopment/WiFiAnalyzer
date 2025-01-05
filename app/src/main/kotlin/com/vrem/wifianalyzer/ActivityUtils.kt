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
package com.vrem.wifianalyzer

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar

internal fun MainActivity.keepScreenOn() =
    if (MainContext.INSTANCE.settings.keepScreenOn()) {
        this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

internal fun MainActivity.setupToolbar(): Toolbar {
    val toolbar: Toolbar = this.findViewById(R.id.toolbar)
    this.setSupportActionBar(toolbar)
    this.supportActionBar?.let {
        it.setHomeButtonEnabled(true)
        it.setDisplayHomeAsUpEnabled(true)
    }
    return toolbar
}

internal fun makeIntent(action: String): Intent = Intent(action)

@TargetApi(Build.VERSION_CODES.Q)
internal fun MainActivity.startWiFiSettings() =
    this.startActivity(makeIntent(Settings.Panel.ACTION_WIFI))

internal fun MainActivity.startLocationSettings() =
    this.startActivity(makeIntent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

