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
package com.vrem.wifianalyzer.navigation.items

import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.NavigationMenu

internal class FragmentItem(val fragment: Fragment, override val registered: Boolean = true, override val visibility: Int = View.VISIBLE) :
    NavigationItem {

    override fun activate(mainActivity: MainActivity, menuItem: MenuItem, navigationMenu: NavigationMenu) {
        val fragmentManager: FragmentManager = mainActivity.supportFragmentManager
        if (fragmentManager.isStateSaved) return
        updateMainActivity(mainActivity, menuItem, navigationMenu)
        startFragment(fragmentManager)
    }

    private fun startFragment(fragmentManager: FragmentManager) {
        fragmentManager.commit {
            replace(R.id.main_fragment, fragment)
        }
    }

    private fun updateMainActivity(mainActivity: MainActivity, menuItem: MenuItem, navigationMenu: NavigationMenu) {
        mainActivity.currentNavigationMenu(navigationMenu)
        mainActivity.title = menuItem.title
        mainActivity.updateActionBar()
        mainActivity.mainConnectionVisibility(visibility)
    }

}