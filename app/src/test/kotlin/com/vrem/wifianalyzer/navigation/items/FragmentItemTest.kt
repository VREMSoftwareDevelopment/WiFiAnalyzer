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

import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.NavigationMenu
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class FragmentItemTest {
    private val title = "title"
    private val fragment: Fragment = mock()
    private val mainActivity: MainActivity = mock()
    private val menuItem: MenuItem = mock()
    private val fragmentManager: FragmentManager = mock()
    private val fragmentTransaction: FragmentTransaction = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(fragment)
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(fragmentManager)
        verifyNoMoreInteractions(fragmentTransaction)
    }

    @Test
    fun testActivateWithStateSaved() {
        // setup
        val fixture = FragmentItem(fragment, true, View.VISIBLE)
        val navigationMenu = NavigationMenu.ACCESS_POINTS
        whenever(mainActivity.supportFragmentManager).thenReturn(fragmentManager)
        whenever(fragmentManager.isStateSaved).thenReturn(true)
        // execute
        fixture.activate(mainActivity, menuItem, navigationMenu)
        // validate
        verify(mainActivity).supportFragmentManager
        verify(fragmentManager).isStateSaved
        verifyFragmentManagerIsNotCalled()
        verifyNoChangesToMainActivity(navigationMenu)
    }

    @Test
    fun testActivateWithStateNotSaved() {
        // setup
        val fixture = FragmentItem(fragment, true, View.VISIBLE)
        val navigationMenu = NavigationMenu.ACCESS_POINTS
        whenever(mainActivity.supportFragmentManager).thenReturn(fragmentManager)
        whenever(fragmentManager.isStateSaved).thenReturn(false)
        whenever(menuItem.title).thenReturn(title)
        withFragmentTransaction()
        // execute
        fixture.activate(mainActivity, menuItem, navigationMenu)
        // validate
        verify(mainActivity).supportFragmentManager
        verify(fragmentManager).isStateSaved
        verify(menuItem).title
        verifyFragmentManager()
        verifyMainActivityChanges(navigationMenu)
    }

    @Test
    fun testRegisteredFalse() {
        // setup
        val fixture = FragmentItem(fragment, false, View.VISIBLE)
        // execute & validate
        assertFalse(fixture.registered)
    }

    @Test
    fun testRegisteredTrue() {
        // setup
        val fixture = FragmentItem(fragment, true, View.VISIBLE)
        // execute & validate
        assertTrue(fixture.registered)
    }

    @Test
    fun testVisibility() {
        // setup
        val fixture = FragmentItem(fragment, false, View.INVISIBLE)
        // execute & validate
        assertEquals(View.INVISIBLE, fixture.visibility)
    }

    private fun withFragmentTransaction() {
        whenever(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.replace(R.id.main_fragment, fragment)).thenReturn(fragmentTransaction)
    }

    private fun verifyFragmentManager() {
        verify(fragmentManager).beginTransaction()
        verify(fragmentTransaction).replace(R.id.main_fragment, fragment)
        verify(fragmentTransaction).commit()
    }

    private fun verifyMainActivityChanges(navigationMenu: NavigationMenu) {
        verify(mainActivity).currentNavigationMenu(navigationMenu)
        verify(mainActivity).title = title
        verify(mainActivity).updateActionBar()
        verify(mainActivity).mainConnectionVisibility(View.VISIBLE)
    }

    private fun verifyFragmentManagerIsNotCalled() {
        verify(fragmentManager, never()).beginTransaction()
        verify(fragmentTransaction, never()).replace(R.id.main_fragment, fragment)
        verify(fragmentTransaction, never()).commit()
    }

    private fun verifyNoChangesToMainActivity(navigationMenu: NavigationMenu) {
        verify(mainActivity, never()).currentNavigationMenu(navigationMenu)
        verify(mainActivity, never()).title = title
        verify(mainActivity, never()).updateActionBar()
    }
}