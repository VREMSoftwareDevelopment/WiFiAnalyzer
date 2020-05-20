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
package com.vrem.wifianalyzer.navigation.items

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.MenuItem
import android.view.View
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.navigation.NavigationMenu
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.*

class PortAuthorityItemTest {
    private val mainActivity: MainActivity = mock(MainActivity::class.java)
    private val context: Context = mock(Context::class.java)
    private val intent: Intent = mock(Intent::class.java)
    private val menuItem: MenuItem = mock(MenuItem::class.java)
    private val packageManager: PackageManager = mock(PackageManager::class.java)
    private val fixture: PortAuthorityItem = spy(PortAuthorityItem())

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(context)
        verifyNoMoreInteractions(intent)
        verifyNoMoreInteractions(packageManager)
    }

    @Test
    fun testActivateWhenPortAuthorityDonate() {
        // setup
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)).thenReturn(intent)
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY)
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verify(context).startActivity(intent)
        verify(mainActivity).applicationContext
        verify(context).packageManager
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)
    }

    @Test
    fun testActivateWhenPortAuthorityFree() {
        // setup
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)).thenReturn(null)
        whenever(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_FREE)).thenReturn(intent)
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY)
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verify(context).startActivity(intent)
        verify(mainActivity).applicationContext
        verify(context).packageManager
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_FREE)
    }

    @Test
    fun testActivateWhenNoPortAuthority() {
        // setup
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)).thenReturn(null)
        whenever(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_FREE)).thenReturn(null)
        doReturn(intent).whenever(fixture).redirectToPlayStore()
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY)
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verify(context).startActivity(intent)
        verify(mainActivity).applicationContext
        verify(context).packageManager
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_FREE)
        verify(fixture).redirectToPlayStore()
    }

    @Test
    fun testRegistered() {
        // setup
        // execute
        val actual: Boolean = fixture.registered
        // validate
        Assert.assertFalse(actual)
    }

    @Test
    fun testVisibility() {
        // setup
        // execute
        val actual: Int = fixture.visibility
        // validate
        Assert.assertEquals(View.GONE, actual)
    }
}