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
package com.vrem.wifianalyzer

import androidx.test.espresso.Espresso.pressBack

internal class SettingsInstrumentedTest : Runnable {
    override fun run() {
        selectMenuItem(R.id.nav_drawer_settings, "Settings")
        verifySettings()
        pressBack()
    }

    private fun verifySettings() {
        listOf(
            "Scan Interval",
            "Sort Access Points By",
            "Group Access Points By",
            "Connection Display",
            "Access Point Display",
            "Graph Maximum Signal Strength",
            "Channel Graph Legend Display",
            "Time Graph Legend Display",
            "Theme",
            "Keep screen on",
            "Country",
            "Language",
            "Reset",
        ).forEach { scrollToAndVerify(it) }
    }
}
