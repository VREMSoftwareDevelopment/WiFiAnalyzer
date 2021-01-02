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
package com.vrem.wifianalyzer.navigation.items

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.MenuItem
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.navigation.NavigationMenu

@OpenClass
internal class PortAuthorityItem : NavigationItem {
    override fun activate(mainActivity: MainActivity, menuItem: MenuItem, navigationMenu: NavigationMenu) {
        try {
            val context: Context = mainActivity.applicationContext
            val packageManager: PackageManager = context.packageManager
            val intent: Intent = packageManager.getLaunchIntentForPackage(PORT_AUTHORITY_DONATE)
                    ?: let {
                        packageManager.getLaunchIntentForPackage(PORT_AUTHORITY_FREE)
                                ?: redirectToPlayStore()
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // No Store or Port Authority Available
        }
    }

    fun redirectToPlayStore(): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=$PORT_AUTHORITY_FREE")
        return intent
    }

    companion object {
        private const val PORT_AUTHORITY = "com.aaronjwood.portauthority."
        private const val PORT_AUTHORITY_FREE = PORT_AUTHORITY + "free"
        private const val PORT_AUTHORITY_DONATE = PORT_AUTHORITY + "donate"
    }
}